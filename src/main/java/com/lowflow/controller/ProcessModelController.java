package com.lowflow.controller;

import com.lowflow.util.BpmnUtil;
import com.lowflow.pojo.ProcessModel;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Objects;

@RestController
@RequestMapping("/model")
public class ProcessModelController {

    /**
     * 转为bpmn，并下载
     * @param processModel
     * @throws IOException
     */
    @PostMapping("/download")
    public void downloadXml(@RequestBody ProcessModel processModel) throws IOException {
        BpmnModel bpmnModel = BpmnUtil.toBpmnModel(processModel);
        byte[] xmlBytes = new BpmnXMLConverter().convertToXML(bpmnModel);
        if (Objects.nonNull(xmlBytes)) {
            String fileName = processModel.getName().replaceAll(" ", "_") + ".bpmn20.xml";
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            if (Objects.nonNull(response)) {
                response.setContentType("application/xml");
                response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
                response.setHeader("Content-Disposition", "attachment; filename=file.bpmn20.xml; filename*=" + URLEncoder.encode(fileName, "UTF-8"));
                ServletOutputStream servletOutputStream = response.getOutputStream();
                BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(xmlBytes));
                byte[] buffer = new byte[8096];
                while (true) {
                    int count = in.read(buffer);
                    if (count == -1) {
                        break;
                    }
                    servletOutputStream.write(buffer, 0, count);
                }
                // 刷新并关闭流
                servletOutputStream.flush();
                servletOutputStream.close();
            }
        }
    }
}

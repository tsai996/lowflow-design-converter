# AGENTS.md

## Project overview

This repository is a small Java 8 / Spring Boot 2.3.12 service that converts lowflow-design JSON models into Flowable 6.8 BPMN XML.

- Entry point: `src/main/java/com/lowflow/LowflowApplication.java`
- HTTP API: `POST /lowflow/model/download`
- Conversion root: `ProcessModel.toBpmnModel()`
- Node conversion: `src/main/java/com/lowflow/pojo/node/`
- Runtime configuration: `src/main/resources/application*.yml`
- Tests: `src/test/java/`
- `public/` only contains README images; it is not frontend source.

## Build and verification

There is no Maven wrapper. Use the installed Maven and JDK 8-compatible source level.

```bash
mvn test
mvn package
```

Run `com.lowflow.LowflowApplication` from the IDE for local development. The `dev` profile is active by default and serves on port `8080` with context path `/lowflow`.

Before finishing a code change, run the narrowest relevant test, then `mvn test` when practical. Do not commit generated `target/` content or IDE metadata.

## Architecture and invariants

`ProcessModel.toBpmnModel()` creates one Flowable `Process`, recursively collects each node's `FlowElement` values through `Node.convert()`, then runs `BpmnAutoLayout`.

- The JSON link to the following node is `next`, matching `Node.next`; do not introduce a second alias without an explicit compatibility requirement.
- Every concrete node must create its Flowable element, create the outgoing `SequenceFlow` when applicable, propagate `branchId`, and recursively append `next.convert()`.
- `Node.buildSequence()` is the shared source/target rule. Fix sequence-link behavior there when the change applies to all node types.
- Exclusive branches are `ConditionNode` entries. `branchId` identifies the merge target after a branch.
- Jackson polymorphism is controlled by `Node`'s `@JsonSubTypes` and the JSON `type` field. Adding a node type requires registering it there and implementing `convert()`.
- Enum `@JsonValue` strings and existing JSON property names are API contracts; preserve them unless the task explicitly changes the payload format.
- Condition expressions use Flowable/JUEL helper calls from `ConditionNode.operatorMap`. Preserve `${...}` wrapping and validate nested `and`/`or` output when changing this code.
- Keep the BPMN target namespace `https://flowable.org/bpmn20` unless engine compatibility explicitly requires otherwise.

## Code style and scope

- Follow the existing package layout under `com.lowflow` and the existing four-space Java indentation.
- Keep Java 8 compatibility: no records, newer language syntax, or post-Java-8 APIs.
- Reuse Lombok and the Spring/Flowable utilities already present; do not add a dependency for a small helper.
- Prefer a focused change in the shared conversion path over duplicated guards in individual node classes.
- Keep controllers thin. BPMN construction belongs in the model/node conversion code, not in the HTTP layer.
- Do not implement placeholders such as `ParallelNode` unless the task requires them.
- Preserve bilingual README content and update both README files when changing documented user behavior.

## Testing notes

Add or update a small JUnit 4 test under `src/test/java` for non-trivial conversion behavior. Assert the generated `BpmnModel` or XML structure; console output alone is not verification.

Current baseline: `LowflowApplicationTest` fails during deserialization because its fixture uses `child` while `Node` exposes `next`. Treat this as a known pre-existing failure unless the task addresses payload compatibility or the stale fixture. Report it clearly instead of hiding it with skipped tests.

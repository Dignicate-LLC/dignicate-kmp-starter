# Runtime Configuration

This directory stores environment-specific runtime config files.

## Purpose

The app supports three environments (`dev`, `stg`, `prod`). Each environment
keeps its own `config.yaml`, and only the selected environment's file is bundled
into the app at build time.

## Current status

- **Active**: Compose Resources + build-time environment replacement.
- Runtime config loading is fully owned by `commonMain`.
- Platform code is responsible only for entry points and `PlatformInfo`.

## File layout

```
config/
  dev/config.yaml
  stg/config.yaml
  prod/config.yaml
```

Each file must follow the same schema:

```yaml
env: dev
apiBaseUrl: https://...
```

## Build-time bundling

The build selects exactly one environment file and copies it to Compose
Resources so the app reads a single runtime config at startup:

```
files/config.yaml
```

- The selected environment is controlled by `-PappEnv=dev|stg|prod` (default: `dev`).
- `core/build.gradle.kts` validates the env and generates
  `build/generated/runtimeConfig/composeResources/files/config.yaml`.

## Startup loading (KMP)

Loading is unified in shared code:

- `loadRuntimeConfigYaml()` — reads `Res.readBytes("files/config.yaml")` from
  Compose Resources in `commonMain`.
- `RuntimeConfigLoader.fromYaml(...)` — shared parsing + validation.
- `loadRuntimeConfig()` — shared, parses and validates the bundled config and
  returns a `RuntimeConfig`.

## Guardrails

Loading is **fail-fast**: if `files/config.yaml` is missing, unparseable, or
fails validation (`env`/`apiBaseUrl` required), `loadRuntimeConfig()` throws and
the app crashes immediately at startup.

## CI checks

- CI runs `:core:assemble` with `appEnv` matrix: `dev`, `stg`, `prod`.
- For each env, CI verifies generated `files/config.yaml` exists.
- CI also verifies invalid env fails: `:core:assemble -PappEnv=qa`.

## Changing the backend URL

Update `apiBaseUrl` in the relevant `config/<env>/config.yaml`. No code change is
needed; the build selects that env and generates `files/config.yaml` for runtime.

## Release checklist (runtime config)

1. Confirm target environment (`dev/stg/prod`) is correct for the build.
2. Run `./gradlew :core:assemble -PappEnv=<env>`.
3. Confirm generated file exists:
   `core/build/generated/runtimeConfig/composeResources/files/config.yaml`.
4. Confirm `env` and `apiBaseUrl` values in `config/<env>/config.yaml` are correct.

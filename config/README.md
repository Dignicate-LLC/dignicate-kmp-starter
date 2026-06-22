# Runtime Configuration

This directory holds the environment-specific runtime configuration for the
mobile app.

## File layout

```
config/
  dev/config.yaml
  stg/config.yaml
  prod/config.yaml
```

Each `config.yaml` must contain:

```yaml
env: "dev"
apiBaseUrl: "https://api.example.com"
```

`env` and `apiBaseUrl` are required.

## Build-time bundling

The selected flavor's config is copied to:

```
runtime/config.yaml
```

inside app assets/bundle.

- Android: `composeApp/build.gradle.kts` wires `copyRuntimeConfig{Dev,Stg,Prod}`
  to corresponding `pre{Dev,Stg,Prod}*Build` tasks.
- iOS: runtime loading expects `runtime/config.yaml` in app bundle.

## Startup loading (KMP)

- `loadRuntimeConfigYaml()` — `expect/actual` file read.
- `RuntimeConfigLoader.fromYaml(...)` — shared parse + validation.
- `loadRuntimeConfig()` — fail-fast load.
- `buildAppConfig(PlatformInfo)` + `startApplication(PlatformInfo)` — shared app init.

If `runtime/config.yaml` is missing or invalid, app startup fails immediately.

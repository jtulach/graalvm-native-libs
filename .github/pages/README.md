# Benchmark dashboard

This static Vue app reads the `window.BENCHMARK_DATA` value published by
`github-action-benchmark` in `data.js`. For local development, run the app from
this directory:

```sh
bun install
bun run dev
```

The page shows an empty-data message until a benchmark-generated `data.js` is
available. To check types and create the static site bundle:

```sh
bun run typecheck
bun run build
```

The development, type-check, and build scripts run through Bun directly and do
not require Node.js or mise. CI uses Node.js with the `typecheck:node` and
`build:node` scripts. The generated `dist/` directory is copied to each
benchmark folder on the `gh-pages` branch; the benchmark action's `data.js`
remains alongside the built page.

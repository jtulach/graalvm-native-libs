window.BENCHMARK_DATA = {
  "lastUpdate": 1790408475836,
  "repoUrl": "https://github.com/jtulach/graalvm-native-libs",
  "entries": {
    "persist JMH": [
      {
        "commit": {
          "author": {
            "email": "jaroslav.tulach@apidesign.org",
            "name": "Jaroslav Tulach",
            "username": "jtulach"
          },
          "committer": {
            "email": "jaroslav.tulach@apidesign.org",
            "name": "Jaroslav Tulach",
            "username": "jtulach"
          },
          "distinct": true,
          "id": "089eda9af48578edf9b1f8452ec0ea3ea547bc90",
          "message": "TEMPORARY: run full benchmarks on push to seed gh-pages (drop before merge)\n\nCo-Authored-By: Claude Sonnet 5 <noreply@anthropic.com>",
          "timestamp": "2026-09-25T18:16:50+02:00",
          "tree_id": "edd1bacbf5780bcdc8afd85ec1a6179833a9c2df",
          "url": "https://github.com/jtulach/graalvm-native-libs/commit/089eda9af48578edf9b1f8452ec0ea3ea547bc90"
        },
        "date": 1790355528999,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.deserializePoint",
            "value": 504200.44291674084,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.serializePoint",
            "value": 3801105.701594591,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializeLine",
            "value": 16879393.428529855,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializePoint",
            "value": 30296111.334792495,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializeLine",
            "value": 6898780.317000411,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializePoint",
            "value": 10733732.15877171,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          }
        ]
      },
      {
        "commit": {
          "author": {
            "name": "Jaroslav Tulach",
            "username": "jtulach",
            "email": "jaroslav.tulach@apidesign.org"
          },
          "committer": {
            "name": "GitHub",
            "username": "web-flow",
            "email": "noreply@github.com"
          },
          "id": "fb15d1d16c1a891fcfb1d64d4f667df7e4713715",
          "message": "Initial benchmarking infrastructure (#8)",
          "timestamp": "2026-09-26T07:29:05Z",
          "url": "https://github.com/jtulach/graalvm-native-libs/commit/fb15d1d16c1a891fcfb1d64d4f667df7e4713715"
        },
        "date": 1790408475199,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.deserializePoint",
            "value": 508943.0209007456,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.serializePoint",
            "value": 3836605.641710684,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializeLine",
            "value": 17404057.974047396,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializePoint",
            "value": 30360014.265375655,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializeLine",
            "value": 6978472.539215437,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializePoint",
            "value": 10735068.220113218,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          }
        ]
      }
    ]
  }
}
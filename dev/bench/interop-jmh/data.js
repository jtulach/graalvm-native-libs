window.BENCHMARK_DATA = {
  "lastUpdate": 1793352118374,
  "repoUrl": "https://github.com/jtulach/graalvm-native-libs",
  "entries": {
    "jvm-interop JMH": [
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
        "date": 1790355527474,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelArrayAccess",
            "value": 86375.231143848,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelInvokeString",
            "value": 296875.54795597406,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localArrayAccess",
            "value": 14334061.82379946,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localInvokeString",
            "value": 25490628.661348496,
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
        "date": 1790408472845,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelArrayAccess",
            "value": 85615.57309353664,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelInvokeString",
            "value": 302150.2515512042,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localArrayAccess",
            "value": 14526524.658255145,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localInvokeString",
            "value": 25529407.999708086,
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
        "date": 1790498962955,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelArrayAccess",
            "value": 83788.30751871479,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelInvokeString",
            "value": 309712.8839216407,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localArrayAccess",
            "value": 14624641.799588276,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localInvokeString",
            "value": 25445194.987973377,
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
        "date": 1790586610035,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelArrayAccess",
            "value": 97373.36385652593,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelInvokeString",
            "value": 307904.95873996336,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localArrayAccess",
            "value": 14935243.664647726,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localInvokeString",
            "value": 25565038.422733467,
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
        "date": 1790673684003,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelArrayAccess",
            "value": 83909.46701650671,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelInvokeString",
            "value": 299556.86249719566,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localArrayAccess",
            "value": 14360114.716817744,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localInvokeString",
            "value": 25571035.228601027,
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
        "date": 1790759757311,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelArrayAccess",
            "value": 88461.70855075664,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelInvokeString",
            "value": 295394.86028526485,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localArrayAccess",
            "value": 14487727.482875424,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localInvokeString",
            "value": 24332641.621968474,
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
        "date": 1790847727258,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelArrayAccess",
            "value": 87417.72949352447,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelInvokeString",
            "value": 303859.79879820155,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localArrayAccess",
            "value": 14572090.719968744,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localInvokeString",
            "value": 25660671.502314504,
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
        "date": 1790932589921,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelArrayAccess",
            "value": 90285.44368821285,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelInvokeString",
            "value": 300833.5168496245,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localArrayAccess",
            "value": 14578946.113725094,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localInvokeString",
            "value": 25592631.763283025,
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
        "date": 1791017158024,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelArrayAccess",
            "value": 126473.31977027743,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelInvokeString",
            "value": 405188.307195629,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localArrayAccess",
            "value": 22957736.775378183,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localInvokeString",
            "value": 40665968.44919839,
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
        "date": 1791104494124,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelArrayAccess",
            "value": 111032.79822650977,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelInvokeString",
            "value": 391243.7035933565,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localArrayAccess",
            "value": 22597270.419249784,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localInvokeString",
            "value": 40045643.73520854,
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
        "date": 1791193794493,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelArrayAccess",
            "value": 109997.79574659123,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelInvokeString",
            "value": 403364.76537362864,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localArrayAccess",
            "value": 22650504.245605517,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localInvokeString",
            "value": 39427240.63640951,
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
        "date": 1791279664597,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelArrayAccess",
            "value": 79734.01558249343,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelInvokeString",
            "value": 276529.7503472166,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localArrayAccess",
            "value": 14468886.128829563,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localInvokeString",
            "value": 23919685.476900924,
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
        "date": 1791365885840,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelArrayAccess",
            "value": 82887.78671393845,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelInvokeString",
            "value": 304744.84001443646,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localArrayAccess",
            "value": 14562923.404324079,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localInvokeString",
            "value": 25519062.99476485,
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
        "date": 1791452731906,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelArrayAccess",
            "value": 94684.23905091577,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelInvokeString",
            "value": 348993.59079691605,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localArrayAccess",
            "value": 18688849.434486363,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localInvokeString",
            "value": 31039897.655670222,
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
        "date": 1791539670087,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelArrayAccess",
            "value": 86164.01923396555,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelInvokeString",
            "value": 295007.5308144467,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localArrayAccess",
            "value": 14593130.218883023,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localInvokeString",
            "value": 25213718.946216997,
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
        "date": 1791624054559,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelArrayAccess",
            "value": 85810.97724567943,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelInvokeString",
            "value": 300631.37214151194,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localArrayAccess",
            "value": 14540461.53174687,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localInvokeString",
            "value": 25665165.4266876,
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
        "date": 1791711137041,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelArrayAccess",
            "value": 81166.63173161806,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelInvokeString",
            "value": 343906.8605464061,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localArrayAccess",
            "value": 18288519.984353874,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localInvokeString",
            "value": 31089795.241511077,
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
        "date": 1791799475728,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelArrayAccess",
            "value": 69528.34597183704,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelInvokeString",
            "value": 220570.93552071854,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localArrayAccess",
            "value": 10586873.135279147,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localInvokeString",
            "value": 18650978.76966226,
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
        "date": 1791885368959,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelArrayAccess",
            "value": 112495.30947099433,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelInvokeString",
            "value": 385079.16793955344,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localArrayAccess",
            "value": 22466063.482722085,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localInvokeString",
            "value": 37671424.07567964,
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
          "id": "a889f0ee61168af1fc94e0b6047d2490130e9f41",
          "message": "Updating to GraalVM 25.4.4.1.1 (#10)",
          "timestamp": "2026-10-28T20:12:37Z",
          "url": "https://github.com/jtulach/graalvm-native-libs/commit/a889f0ee61168af1fc94e0b6047d2490130e9f41"
        },
        "date": 1793222314531,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelArrayAccess",
            "value": 93117.00447847281,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelInvokeString",
            "value": 290614.2690394942,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localArrayAccess",
            "value": 14906718.949926034,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localInvokeString",
            "value": 25499405.213774335,
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
            "name": "Jaroslav Tulach",
            "username": "jtulach",
            "email": "jaroslav.tulach@apidesign.org"
          },
          "id": "789a7f43c6193ede818992615ca46f376df41273",
          "message": "Speeding up by avoiding toByteArray() copy",
          "timestamp": "2026-10-29T06:22:01Z",
          "url": "https://github.com/jtulach/graalvm-native-libs/commit/789a7f43c6193ede818992615ca46f376df41273"
        },
        "date": 1793255748416,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelArrayAccess",
            "value": 115868.83835593809,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelInvokeString",
            "value": 372338.06022468564,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localArrayAccess",
            "value": 21858670.297433764,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localInvokeString",
            "value": 38631332.18303511,
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
            "name": "Jaroslav Tulach",
            "username": "jtulach",
            "email": "jaroslav.tulach@apidesign.org"
          },
          "id": "d6c145a78790c3b095b00b8b6ad27ff46138d537",
          "message": "Adding Kryo I/O benchmark as a baseline",
          "timestamp": "2026-10-29T07:06:43Z",
          "url": "https://github.com/jtulach/graalvm-native-libs/commit/d6c145a78790c3b095b00b8b6ad27ff46138d537"
        },
        "date": 1793258355861,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelArrayAccess",
            "value": 86357.40634958686,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelInvokeString",
            "value": 289533.1226979987,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localArrayAccess",
            "value": 14925402.33592053,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localInvokeString",
            "value": 25222490.468231928,
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
            "name": "Jaroslav Tulach",
            "username": "jtulach",
            "email": "jaroslav.tulach@apidesign.org"
          },
          "id": "d6c145a78790c3b095b00b8b6ad27ff46138d537",
          "message": "Adding Kryo I/O benchmark as a baseline",
          "timestamp": "2026-10-29T07:06:43Z",
          "url": "https://github.com/jtulach/graalvm-native-libs/commit/d6c145a78790c3b095b00b8b6ad27ff46138d537"
        },
        "date": 1793264940283,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelArrayAccess",
            "value": 114881.62159387323,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelInvokeString",
            "value": 382301.33644762513,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localArrayAccess",
            "value": 22862396.33903447,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localInvokeString",
            "value": 40211026.673842795,
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
            "name": "Jaroslav Tulach",
            "username": "jtulach",
            "email": "jaroslav.tulach@apidesign.org"
          },
          "id": "5db49d793dfb88e904ba59ba834e7f656b1774ac",
          "message": "Speed up bench/channel by reusing allocated memory segments",
          "timestamp": "2026-10-30T06:05:35Z",
          "url": "https://github.com/jtulach/graalvm-native-libs/commit/5db49d793dfb88e904ba59ba834e7f656b1774ac"
        },
        "date": 1793341655791,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelArrayAccess",
            "value": 127474.47981903981,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelInvokeString",
            "value": 408063.11157176434,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localArrayAccess",
            "value": 14857877.402459592,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localInvokeString",
            "value": 25229999.310481835,
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
            "name": "Jaroslav Tulach",
            "username": "jtulach",
            "email": "jaroslav.tulach@apidesign.org"
          },
          "id": "4ba69bed7065129709dbbbd381e78e0c9e33e0ea",
          "message": "Avoid realloc when the size matches",
          "timestamp": "2026-10-30T06:33:46Z",
          "url": "https://github.com/jtulach/graalvm-native-libs/commit/4ba69bed7065129709dbbbd381e78e0c9e33e0ea"
        },
        "date": 1793343855778,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelArrayAccess",
            "value": 130004.18141051511,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelInvokeString",
            "value": 451724.76482296805,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localArrayAccess",
            "value": 14447988.538662652,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localInvokeString",
            "value": 25253121.129266147,
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
            "name": "Jaroslav Tulach",
            "username": "jtulach",
            "email": "jaroslav.tulach@apidesign.org"
          },
          "id": "4ba69bed7065129709dbbbd381e78e0c9e33e0ea",
          "message": "Avoid realloc when the size matches",
          "timestamp": "2026-10-30T06:33:46Z",
          "url": "https://github.com/jtulach/graalvm-native-libs/commit/4ba69bed7065129709dbbbd381e78e0c9e33e0ea"
        },
        "date": 1793344960966,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelArrayAccess",
            "value": 100703.06644625422,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelInvokeString",
            "value": 326473.95319112117,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localArrayAccess",
            "value": 14065263.964931523,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localInvokeString",
            "value": 24337354.534022033,
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
            "name": "Jaroslav Tulach",
            "username": "jtulach",
            "email": "jaroslav.tulach@apidesign.org"
          },
          "id": "d6c145a78790c3b095b00b8b6ad27ff46138d537",
          "message": "Adding Kryo I/O benchmark as a baseline",
          "timestamp": "2026-10-29T07:06:43Z",
          "url": "https://github.com/jtulach/graalvm-native-libs/commit/d6c145a78790c3b095b00b8b6ad27ff46138d537"
        },
        "date": 1793352118149,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelArrayAccess",
            "value": 84082.9927015427,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.channelInvokeString",
            "value": 278578.4386062486,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localArrayAccess",
            "value": 14844963.530995356,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.interop.InteropBenchmark.localInvokeString",
            "value": 25573610.715117205,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          }
        ]
      }
    ]
  }
}
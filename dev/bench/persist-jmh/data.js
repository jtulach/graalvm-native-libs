window.BENCHMARK_DATA = {
  "lastUpdate": 1793395126303,
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
        "date": 1790498964941,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.deserializePoint",
            "value": 512867.4230028889,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.serializePoint",
            "value": 3919985.691277685,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializeLine",
            "value": 17750545.94295777,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializePoint",
            "value": 30684816.35651462,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializeLine",
            "value": 6975261.232170522,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializePoint",
            "value": 10742916.849585637,
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
        "date": 1790586612362,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.deserializePoint",
            "value": 731196.4604788568,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.serializePoint",
            "value": 2173457.8777792477,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializeLine",
            "value": 26243191.79102565,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializePoint",
            "value": 40373954.4464273,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializeLine",
            "value": 6476061.240160663,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializePoint",
            "value": 10533558.007310336,
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
        "date": 1790673685950,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.deserializePoint",
            "value": 504037.9593602964,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.serializePoint",
            "value": 3952599.8722334015,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializeLine",
            "value": 17738603.56010937,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializePoint",
            "value": 30840175.817819566,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializeLine",
            "value": 6835657.51550712,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializePoint",
            "value": 10721719.37648748,
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
        "date": 1790759760031,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.deserializePoint",
            "value": 502266.6424659158,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.serializePoint",
            "value": 3573435.560131298,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializeLine",
            "value": 16863133.634307373,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializePoint",
            "value": 29462022.284340624,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializeLine",
            "value": 6849462.73108537,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializePoint",
            "value": 10753984.205123086,
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
        "date": 1790847729692,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.deserializePoint",
            "value": 494504.37999177846,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.serializePoint",
            "value": 3938896.783439934,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializeLine",
            "value": 17572596.402430512,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializePoint",
            "value": 30321517.15612705,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializeLine",
            "value": 6989631.195761596,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializePoint",
            "value": 10732577.636991622,
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
        "date": 1790932592443,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.deserializePoint",
            "value": 509560.58294598525,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.serializePoint",
            "value": 3936778.58254082,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializeLine",
            "value": 17588007.164180744,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializePoint",
            "value": 29017830.60479487,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializeLine",
            "value": 6834963.822038728,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializePoint",
            "value": 10662432.90078623,
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
        "date": 1791017160612,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.deserializePoint",
            "value": 832271.5709750396,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.serializePoint",
            "value": 3707934.624795407,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializeLine",
            "value": 31813449.260143932,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializePoint",
            "value": 53997445.79080745,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializeLine",
            "value": 9877893.8567031,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializePoint",
            "value": 18910073.15050718,
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
        "date": 1791104496493,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.deserializePoint",
            "value": 824923.3263766657,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.serializePoint",
            "value": 2987094.550799912,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializeLine",
            "value": 31120288.107036803,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializePoint",
            "value": 51986267.9002454,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializeLine",
            "value": 9690682.468554916,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializePoint",
            "value": 18187350.83668661,
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
        "date": 1791193797251,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.deserializePoint",
            "value": 812195.03705484,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.serializePoint",
            "value": 3480026.314140283,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializeLine",
            "value": 31707719.694482584,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializePoint",
            "value": 54878928.603590526,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializeLine",
            "value": 9907079.000839306,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializePoint",
            "value": 18086848.386382725,
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
        "date": 1791279666424,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.deserializePoint",
            "value": 506523.15521825175,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.serializePoint",
            "value": 3899283.7141020917,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializeLine",
            "value": 16042514.961890839,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializePoint",
            "value": 28920536.838390246,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializeLine",
            "value": 6752896.449159063,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializePoint",
            "value": 10385131.21945986,
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
        "date": 1791365887751,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.deserializePoint",
            "value": 514177.94566456834,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.serializePoint",
            "value": 3911587.3038646164,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializeLine",
            "value": 17768853.364312213,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializePoint",
            "value": 30728059.88060159,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializeLine",
            "value": 6951449.988872443,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializePoint",
            "value": 10892715.154939704,
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
        "date": 1791452733765,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.deserializePoint",
            "value": 658520.1737333153,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.serializePoint",
            "value": 4322728.471011307,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializeLine",
            "value": 22113574.174682345,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializePoint",
            "value": 36936182.54194769,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializeLine",
            "value": 6855316.622441853,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializePoint",
            "value": 12946863.114596104,
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
        "date": 1791539672104,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.deserializePoint",
            "value": 512710.5087878517,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.serializePoint",
            "value": 3791235.9166958113,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializeLine",
            "value": 17768215.02961206,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializePoint",
            "value": 30395252.54782254,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializeLine",
            "value": 6961411.593678775,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializePoint",
            "value": 10831261.705706222,
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
        "date": 1791624056171,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.deserializePoint",
            "value": 505736.0719479237,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.serializePoint",
            "value": 3940859.9836886567,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializeLine",
            "value": 17425254.881784625,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializePoint",
            "value": 30440063.610012304,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializeLine",
            "value": 6979548.446781293,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializePoint",
            "value": 10830201.14586232,
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
        "date": 1791711139292,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.deserializePoint",
            "value": 665474.1678147671,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.serializePoint",
            "value": 4222632.152260324,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializeLine",
            "value": 21368124.766768906,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializePoint",
            "value": 36877935.54538211,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializeLine",
            "value": 7161382.616448325,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializePoint",
            "value": 13060317.508153034,
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
        "date": 1791799477827,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.deserializePoint",
            "value": 484990.0000329554,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.serializePoint",
            "value": 1724596.6823879788,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializeLine",
            "value": 16079401.153441982,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializePoint",
            "value": 25055421.342302125,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializeLine",
            "value": 5174706.903685097,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializePoint",
            "value": 7904645.770317722,
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
        "date": 1791885371829,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.deserializePoint",
            "value": 784249.5433134111,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.serializePoint",
            "value": 3145837.8001531884,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializeLine",
            "value": 30379270.806718647,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializePoint",
            "value": 51498018.05545515,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializeLine",
            "value": 10070251.153953264,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializePoint",
            "value": 17634220.354464218,
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
        "date": 1793222316848,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.deserializePoint",
            "value": 484836.1334068477,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.serializePoint",
            "value": 3182953.2251466275,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializeLine",
            "value": 17890572.00207874,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializePoint",
            "value": 29490632.98521839,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializeLine",
            "value": 7356533.2639335925,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializePoint",
            "value": 10131398.237135151,
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
        "date": 1793255751600,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.deserializePoint",
            "value": 788103.9976190781,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.serializePoint",
            "value": 3488602.3686329243,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializeLine",
            "value": 33012182.74614917,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializePoint",
            "value": 51468062.12858798,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializeLine",
            "value": 12104052.793269373,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializePoint",
            "value": 19997672.65349745,
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
        "date": 1793258358338,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.persist.KryoComparisonBenchmark.deserializePoint",
            "value": 19167227.565813843,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.KryoComparisonBenchmark.serializePoint",
            "value": 18027653.21384875,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.deserializePoint",
            "value": 490263.07188112196,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.serializePoint",
            "value": 3368178.6012814757,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializeLine",
            "value": 17582653.051991124,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializePoint",
            "value": 27503526.326227944,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializeLine",
            "value": 8370695.428502127,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializePoint",
            "value": 12237209.144438615,
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
        "date": 1793264942535,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.persist.KryoComparisonBenchmark.deserializePoint",
            "value": 31890242.734661996,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.KryoComparisonBenchmark.serializePoint",
            "value": 32843821.473047167,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.deserializePoint",
            "value": 787921.9630217198,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.serializePoint",
            "value": 3850621.1491342043,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializeLine",
            "value": 31595264.315132428,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializePoint",
            "value": 51064456.43950889,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializeLine",
            "value": 12042261.313952995,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializePoint",
            "value": 21436128.814325783,
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
        "date": 1793341658063,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.persist.KryoComparisonBenchmark.deserializePoint",
            "value": 19750587.17786634,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.KryoComparisonBenchmark.serializePoint",
            "value": 17149839.840397563,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.deserializePoint",
            "value": 508245.46995552006,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.serializePoint",
            "value": 3396486.195582483,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializeLine",
            "value": 18157980.389650892,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializePoint",
            "value": 30269469.214110345,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializeLine",
            "value": 8512316.266667439,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializePoint",
            "value": 12285554.246653555,
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
        "date": 1793343857757,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.persist.KryoComparisonBenchmark.deserializePoint",
            "value": 27779747.961938847,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.KryoComparisonBenchmark.serializePoint",
            "value": 30510713.753460657,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.deserializePoint",
            "value": 740853.6416472133,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.serializePoint",
            "value": 2092457.431219584,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializeLine",
            "value": 22684299.071805764,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializePoint",
            "value": 37609811.66586683,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializeLine",
            "value": 7526901.580256022,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializePoint",
            "value": 9344265.516702598,
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
        "date": 1793344963333,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.persist.KryoComparisonBenchmark.deserializePoint",
            "value": 20194014.65518209,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.KryoComparisonBenchmark.serializePoint",
            "value": 19332278.636978026,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.deserializePoint",
            "value": 463160.28755032696,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.serializePoint",
            "value": 2136849.433555389,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializeLine",
            "value": 16056465.243788525,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializePoint",
            "value": 23555193.516708355,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializeLine",
            "value": 6513059.512314502,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializePoint",
            "value": 10239251.88814495,
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
        "date": 1793352119843,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.persist.KryoComparisonBenchmark.deserializePoint",
            "value": 19327673.987887222,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.KryoComparisonBenchmark.serializePoint",
            "value": 18333002.776793342,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.deserializePoint",
            "value": 484143.52582062205,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.serializePoint",
            "value": 3239153.11896318,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializeLine",
            "value": 18177508.364951413,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializePoint",
            "value": 29228784.502816964,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializeLine",
            "value": 7899777.60850893,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializePoint",
            "value": 11742723.591482932,
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
          "id": "efa58e7272d538c0541a405573395f3f4de6b64e",
          "message": "Increase the default number of iterations otherwise JVM initialization dominates the wall clock time",
          "timestamp": "2026-10-30T15:58:24Z",
          "url": "https://github.com/jtulach/graalvm-native-libs/commit/efa58e7272d538c0541a405573395f3f4de6b64e"
        },
        "date": 1793376454505,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.persist.KryoComparisonBenchmark.deserializePoint",
            "value": 32920659.658841714,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.KryoComparisonBenchmark.serializePoint",
            "value": 32341928.820370734,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.deserializePoint",
            "value": 780852.1565923957,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.serializePoint",
            "value": 3219300.045565982,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializeLine",
            "value": 30824814.949229468,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializePoint",
            "value": 50680861.88945941,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializeLine",
            "value": 11828656.158878028,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializePoint",
            "value": 21438662.87701996,
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
          "id": "673695ac5d3832fdcb7eed1078ce8152fa6d846c",
          "message": "Measure mean, not median",
          "timestamp": "2026-10-30T21:05:27Z",
          "url": "https://github.com/jtulach/graalvm-native-libs/commit/673695ac5d3832fdcb7eed1078ce8152fa6d846c"
        },
        "date": 1793395126089,
        "tool": "jmh",
        "benches": [
          {
            "name": "org.apidesign.bench.persist.KryoComparisonBenchmark.deserializePoint",
            "value": 18916366.66828403,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.KryoComparisonBenchmark.serializePoint",
            "value": 17963035.223789103,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.deserializePoint",
            "value": 501753.16118191375,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.ObjectStreamComparisonBenchmark.serializePoint",
            "value": 3479883.0007239613,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializeLine",
            "value": 18068375.706728905,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.deserializePoint",
            "value": 28320388.67833601,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializeLine",
            "value": 8385678.346907764,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          },
          {
            "name": "org.apidesign.bench.persist.PersistBenchmark.serializePoint",
            "value": 12104740.336938754,
            "unit": "ops/s",
            "extra": "iterations: 3\nforks: 1\nthreads: 1"
          }
        ]
      }
    ]
  }
}
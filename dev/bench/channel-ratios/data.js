window.BENCHMARK_DATA = {
  "lastUpdate": 1793343853529,
  "repoUrl": "https://github.com/jtulach/graalvm-native-libs",
  "entries": {
    "jvm-channel ratios": [
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
        "date": 1790355525478,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "jvm-channel: mock round trip",
            "value": 5019,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: hotspot baseline",
            "value": 100,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: mock/hotspot ratio",
            "value": 50,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native (NI+HotSpot) round trip",
            "value": 13605,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: native/mock ratio",
            "value": 2,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native/hotspot ratio",
            "value": 136,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (invokeString)",
            "value": 85.86301174635203,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (arrayAccess)",
            "value": 165.95106761483197,
            "unit": "x"
          },
          {
            "name": "image size: bench-channel",
            "value": 30280248,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmlauncher",
            "value": 29166136,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmchannel",
            "value": 30280248,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvminterop",
            "value": 46467640,
            "unit": "bytes"
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
        "date": 1790408470136,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "jvm-channel: mock round trip",
            "value": 4989,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: hotspot baseline",
            "value": 100,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: mock/hotspot ratio",
            "value": 49,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native (NI+HotSpot) round trip",
            "value": 12624,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: native/mock ratio",
            "value": 2,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native/hotspot ratio",
            "value": 126,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (invokeString)",
            "value": 84.49242676000792,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (arrayAccess)",
            "value": 169.67152275421483,
            "unit": "x"
          },
          {
            "name": "image size: bench-channel",
            "value": 30280248,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmlauncher",
            "value": 29231672,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmchannel",
            "value": 30280248,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvminterop",
            "value": 46467640,
            "unit": "bytes"
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
        "date": 1790498960770,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "jvm-channel: mock round trip",
            "value": 4860,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: hotspot baseline",
            "value": 100,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: mock/hotspot ratio",
            "value": 48,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native (NI+HotSpot) round trip",
            "value": 11622,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: native/mock ratio",
            "value": 2,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native/hotspot ratio",
            "value": 116,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (invokeString)",
            "value": 82.15736673845048,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (arrayAccess)",
            "value": 174.54275223690067,
            "unit": "x"
          },
          {
            "name": "image size: bench-channel",
            "value": 30280248,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmlauncher",
            "value": 29231672,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmchannel",
            "value": 30280248,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvminterop",
            "value": 46467640,
            "unit": "bytes"
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
        "date": 1790586607363,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "jvm-channel: mock round trip",
            "value": 3598,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: hotspot baseline",
            "value": 70,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: mock/hotspot ratio",
            "value": 51,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native (NI+HotSpot) round trip",
            "value": 6071,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: native/mock ratio",
            "value": 1,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native/hotspot ratio",
            "value": 86,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (invokeString)",
            "value": 83.02899221679651,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (arrayAccess)",
            "value": 153.3812027553444,
            "unit": "x"
          },
          {
            "name": "image size: bench-channel",
            "value": 30280248,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmlauncher",
            "value": 29231672,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmchannel",
            "value": 30280248,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvminterop",
            "value": 46467640,
            "unit": "bytes"
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
        "date": 1790673681532,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "jvm-channel: mock round trip",
            "value": 5050,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: hotspot baseline",
            "value": 100,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: mock/hotspot ratio",
            "value": 50,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native (NI+HotSpot) round trip",
            "value": 12363,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: native/mock ratio",
            "value": 2,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native/hotspot ratio",
            "value": 123,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (invokeString)",
            "value": 85.36287573395323,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (arrayAccess)",
            "value": 171.1381948593812,
            "unit": "x"
          },
          {
            "name": "image size: bench-channel",
            "value": 30280248,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmlauncher",
            "value": 29231672,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmchannel",
            "value": 30280248,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvminterop",
            "value": 46467640,
            "unit": "bytes"
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
        "date": 1790759754105,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "jvm-channel: mock round trip",
            "value": 5019,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: hotspot baseline",
            "value": 101,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: mock/hotspot ratio",
            "value": 49,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native (NI+HotSpot) round trip",
            "value": 12503,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: native/mock ratio",
            "value": 2,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native/hotspot ratio",
            "value": 123,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (invokeString)",
            "value": 82.37327351759023,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (arrayAccess)",
            "value": 163.773995779912,
            "unit": "x"
          },
          {
            "name": "image size: bench-channel",
            "value": 30280248,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmlauncher",
            "value": 29231672,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmchannel",
            "value": 30280248,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvminterop",
            "value": 46467640,
            "unit": "bytes"
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
        "date": 1790847724273,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "jvm-channel: mock round trip",
            "value": 5090,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: hotspot baseline",
            "value": 150,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: mock/hotspot ratio",
            "value": 33,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native (NI+HotSpot) round trip",
            "value": 13174,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: native/mock ratio",
            "value": 2,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native/hotspot ratio",
            "value": 87,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (invokeString)",
            "value": 84.44905052858338,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (arrayAccess)",
            "value": 166.69491194058273,
            "unit": "x"
          },
          {
            "name": "image size: bench-channel",
            "value": 30280248,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmlauncher",
            "value": 29166136,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmchannel",
            "value": 30280248,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvminterop",
            "value": 46467640,
            "unit": "bytes"
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
        "date": 1790932587104,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "jvm-channel: mock round trip",
            "value": 5060,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: hotspot baseline",
            "value": 100,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: mock/hotspot ratio",
            "value": 50,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native (NI+HotSpot) round trip",
            "value": 11652,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: native/mock ratio",
            "value": 2,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native/hotspot ratio",
            "value": 116,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (invokeString)",
            "value": 85.07240825853799,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (arrayAccess)",
            "value": 161.47615294522208,
            "unit": "x"
          },
          {
            "name": "image size: bench-channel",
            "value": 30280248,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmlauncher",
            "value": 29166136,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmchannel",
            "value": 30280248,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvminterop",
            "value": 46467640,
            "unit": "bytes"
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
        "date": 1791017155844,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "jvm-channel: mock round trip",
            "value": 3546,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: hotspot baseline",
            "value": 70,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: mock/hotspot ratio",
            "value": 50,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native (NI+HotSpot) round trip",
            "value": 5708,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: native/mock ratio",
            "value": 1,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native/hotspot ratio",
            "value": 81,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (invokeString)",
            "value": 100.36313419470036,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (arrayAccess)",
            "value": 181.52237022858233,
            "unit": "x"
          },
          {
            "name": "image size: bench-channel",
            "value": 30280248,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmlauncher",
            "value": 29166136,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmchannel",
            "value": 30280248,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvminterop",
            "value": 46467640,
            "unit": "bytes"
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
        "date": 1791104491706,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "jvm-channel: mock round trip",
            "value": 3496,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: hotspot baseline",
            "value": 70,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: mock/hotspot ratio",
            "value": 49,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native (NI+HotSpot) round trip",
            "value": 5929,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: native/mock ratio",
            "value": 1,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native/hotspot ratio",
            "value": 84,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (invokeString)",
            "value": 102.35473022929573,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (arrayAccess)",
            "value": 203.51887712629534,
            "unit": "x"
          },
          {
            "name": "image size: bench-channel",
            "value": 30280248,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmlauncher",
            "value": 29231672,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmchannel",
            "value": 30280248,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvminterop",
            "value": 46467640,
            "unit": "bytes"
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
        "date": 1791193792192,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "jvm-channel: mock round trip",
            "value": 3586,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: hotspot baseline",
            "value": 70,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: mock/hotspot ratio",
            "value": 51,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native (NI+HotSpot) round trip",
            "value": 5899,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: native/mock ratio",
            "value": 1,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native/hotspot ratio",
            "value": 84,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (invokeString)",
            "value": 97.7458717790803,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (arrayAccess)",
            "value": 205.9178012783719,
            "unit": "x"
          },
          {
            "name": "image size: bench-channel",
            "value": 30280248,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmlauncher",
            "value": 29231672,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmchannel",
            "value": 30280248,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvminterop",
            "value": 46467640,
            "unit": "bytes"
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
        "date": 1791279662455,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "jvm-channel: mock round trip",
            "value": 5768,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: hotspot baseline",
            "value": 111,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: mock/hotspot ratio",
            "value": 51,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native (NI+HotSpot) round trip",
            "value": 10075,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: native/mock ratio",
            "value": 1,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native/hotspot ratio",
            "value": 90,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (invokeString)",
            "value": 86.4995012177419,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (arrayAccess)",
            "value": 181.46441042919682,
            "unit": "x"
          },
          {
            "name": "image size: bench-channel",
            "value": 30280248,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmlauncher",
            "value": 29166136,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmchannel",
            "value": 30280248,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvminterop",
            "value": 46467640,
            "unit": "bytes"
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
        "date": 1791365883601,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "jvm-channel: mock round trip",
            "value": 5009,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: hotspot baseline",
            "value": 100,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: mock/hotspot ratio",
            "value": 50,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native (NI+HotSpot) round trip",
            "value": 12143,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: native/mock ratio",
            "value": 2,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native/hotspot ratio",
            "value": 121,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (invokeString)",
            "value": 83.73911431463763,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (arrayAccess)",
            "value": 175.6944416260444,
            "unit": "x"
          },
          {
            "name": "image size: bench-channel",
            "value": 30280248,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmlauncher",
            "value": 29231672,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmchannel",
            "value": 30280248,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvminterop",
            "value": 46467640,
            "unit": "bytes"
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
        "date": 1791452729842,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "jvm-channel: mock round trip",
            "value": 4677,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: hotspot baseline",
            "value": 90,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: mock/hotspot ratio",
            "value": 51,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native (NI+HotSpot) round trip",
            "value": 7852,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: native/mock ratio",
            "value": 1,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native/hotspot ratio",
            "value": 87,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (invokeString)",
            "value": 88.94116818819388,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (arrayAccess)",
            "value": 197.3807850368483,
            "unit": "x"
          },
          {
            "name": "image size: bench-channel",
            "value": 30280248,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmlauncher",
            "value": 29166136,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmchannel",
            "value": 30280248,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvminterop",
            "value": 46467640,
            "unit": "bytes"
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
        "date": 1791539667616,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "jvm-channel: mock round trip",
            "value": 4979,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: hotspot baseline",
            "value": 100,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: mock/hotspot ratio",
            "value": 49,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native (NI+HotSpot) round trip",
            "value": 12904,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: native/mock ratio",
            "value": 2,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native/hotspot ratio",
            "value": 129,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (invokeString)",
            "value": 85.46805187178722,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (arrayAccess)",
            "value": 169.3645485508,
            "unit": "x"
          },
          {
            "name": "image size: bench-channel",
            "value": 30280248,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmlauncher",
            "value": 29231672,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmchannel",
            "value": 30280248,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvminterop",
            "value": 46467640,
            "unit": "bytes"
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
        "date": 1791624052598,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "jvm-channel: mock round trip",
            "value": 5009,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: hotspot baseline",
            "value": 110,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: mock/hotspot ratio",
            "value": 45,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native (NI+HotSpot) round trip",
            "value": 13556,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: native/mock ratio",
            "value": 2,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native/hotspot ratio",
            "value": 123,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (invokeString)",
            "value": 85.37088210011096,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (arrayAccess)",
            "value": 169.4475695121976,
            "unit": "x"
          },
          {
            "name": "image size: bench-channel",
            "value": 30280248,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmlauncher",
            "value": 29231672,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmchannel",
            "value": 30280248,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvminterop",
            "value": 46467640,
            "unit": "bytes"
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
        "date": 1791711134395,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "jvm-channel: mock round trip",
            "value": 5809,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: hotspot baseline",
            "value": 90,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: mock/hotspot ratio",
            "value": 64,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native (NI+HotSpot) round trip",
            "value": 7712,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: native/mock ratio",
            "value": 1,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native/hotspot ratio",
            "value": 85,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (invokeString)",
            "value": 90.40178841479052,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (arrayAccess)",
            "value": 225.3206717364578,
            "unit": "x"
          },
          {
            "name": "image size: bench-channel",
            "value": 30280248,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmlauncher",
            "value": 29166136,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmchannel",
            "value": 30280248,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvminterop",
            "value": 46467640,
            "unit": "bytes"
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
        "date": 1791799473277,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "jvm-channel: mock round trip",
            "value": 5100,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: hotspot baseline",
            "value": 72,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: mock/hotspot ratio",
            "value": 70,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native (NI+HotSpot) round trip",
            "value": 7917,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: native/mock ratio",
            "value": 1,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native/hotspot ratio",
            "value": 109,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (invokeString)",
            "value": 84.55773525025626,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (arrayAccess)",
            "value": 152.26700689194357,
            "unit": "x"
          },
          {
            "name": "image size: bench-channel",
            "value": 30280248,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmlauncher",
            "value": 29166136,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmchannel",
            "value": 30280248,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvminterop",
            "value": 46467640,
            "unit": "bytes"
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
        "date": 1791885366521,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "jvm-channel: mock round trip",
            "value": 4336,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: hotspot baseline",
            "value": 80,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: mock/hotspot ratio",
            "value": 54,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native (NI+HotSpot) round trip",
            "value": 6139,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: native/mock ratio",
            "value": 1,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native/hotspot ratio",
            "value": 76,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (invokeString)",
            "value": 97.82773832520846,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (arrayAccess)",
            "value": 199.7066685568318,
            "unit": "x"
          },
          {
            "name": "image size: bench-channel",
            "value": 30280248,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmlauncher",
            "value": 29231672,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmchannel",
            "value": 30280248,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvminterop",
            "value": 46467640,
            "unit": "bytes"
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
        "date": 1793222310373,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "jvm-channel: mock round trip",
            "value": 5180,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: hotspot baseline",
            "value": 100,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: mock/hotspot ratio",
            "value": 51,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native (NI+HotSpot) round trip",
            "value": 12614,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: native/mock ratio",
            "value": 2,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native/hotspot ratio",
            "value": 126,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (invokeString)",
            "value": 87.7431287116497,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (arrayAccess)",
            "value": 160.08589444446994,
            "unit": "x"
          },
          {
            "name": "image size: bench-channel",
            "value": 22415928,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmlauncher",
            "value": 21301816,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmchannel",
            "value": 22284856,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvminterop",
            "value": 34933304,
            "unit": "bytes"
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
        "date": 1793255745850,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "jvm-channel: mock round trip",
            "value": 3846,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: hotspot baseline",
            "value": 70,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: mock/hotspot ratio",
            "value": 54,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native (NI+HotSpot) round trip",
            "value": 5789,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: native/mock ratio",
            "value": 1,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native/hotspot ratio",
            "value": 82,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (invokeString)",
            "value": 103.75337981758625,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (arrayAccess)",
            "value": 188.65012032213528,
            "unit": "x"
          },
          {
            "name": "image size: bench-channel",
            "value": 22415928,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmlauncher",
            "value": 21301816,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmchannel",
            "value": 22284856,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvminterop",
            "value": 34933304,
            "unit": "bytes"
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
        "date": 1793258352096,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "jvm-channel: mock round trip",
            "value": 8657,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: hotspot baseline",
            "value": 100,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: mock/hotspot ratio",
            "value": 86,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native (NI+HotSpot) round trip",
            "value": 12593,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: native/mock ratio",
            "value": 1,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native/hotspot ratio",
            "value": 125,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (invokeString)",
            "value": 87.11435235180527,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (arrayAccess)",
            "value": 172.8329157490026,
            "unit": "x"
          },
          {
            "name": "image size: bench-channel",
            "value": 22415928,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmlauncher",
            "value": 21301816,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmchannel",
            "value": 22284856,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvminterop",
            "value": 34933304,
            "unit": "bytes"
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
        "date": 1793264937747,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "jvm-channel: mock round trip",
            "value": 3795,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: hotspot baseline",
            "value": 70,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: mock/hotspot ratio",
            "value": 54,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native (NI+HotSpot) round trip",
            "value": 5799,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: native/mock ratio",
            "value": 1,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native/hotspot ratio",
            "value": 82,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (invokeString)",
            "value": 105.18149647994146,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (arrayAccess)",
            "value": 199.00830108280564,
            "unit": "x"
          },
          {
            "name": "image size: bench-channel",
            "value": 22415928,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmlauncher",
            "value": 21301816,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmchannel",
            "value": 22284856,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvminterop",
            "value": 34933304,
            "unit": "bytes"
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
        "date": 1793341652146,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "jvm-channel: mock round trip",
            "value": 1122,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: hotspot baseline",
            "value": 150,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: mock/hotspot ratio",
            "value": 7,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native (NI+HotSpot) round trip",
            "value": 12524,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: native/mock ratio",
            "value": 11,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native/hotspot ratio",
            "value": 83,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (invokeString)",
            "value": 61.82866962245555,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (arrayAccess)",
            "value": 116.55570137294565,
            "unit": "x"
          },
          {
            "name": "image size: bench-channel",
            "value": 22415928,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmlauncher",
            "value": 21301816,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmchannel",
            "value": 22284856,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvminterop",
            "value": 34933304,
            "unit": "bytes"
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
        "date": 1793343852898,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "jvm-channel: mock round trip",
            "value": 1454,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: hotspot baseline",
            "value": 70,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: mock/hotspot ratio",
            "value": 20,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native (NI+HotSpot) round trip",
            "value": 5514,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: native/mock ratio",
            "value": 3,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native/hotspot ratio",
            "value": 78,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (invokeString)",
            "value": 55.90377835309273,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (arrayAccess)",
            "value": 111.13479875728102,
            "unit": "x"
          },
          {
            "name": "image size: bench-channel",
            "value": 22415928,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmlauncher",
            "value": 21301816,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvmchannel",
            "value": 22284856,
            "unit": "bytes"
          },
          {
            "name": "image size: demo-jvminterop",
            "value": 34933304,
            "unit": "bytes"
          }
        ]
      }
    ]
  }
}
window.BENCHMARK_DATA = {
  "lastUpdate": 1791017156433,
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
      }
    ]
  }
}
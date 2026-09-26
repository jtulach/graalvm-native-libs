window.BENCHMARK_DATA = {
  "lastUpdate": 1790408471017,
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
      }
    ]
  }
}
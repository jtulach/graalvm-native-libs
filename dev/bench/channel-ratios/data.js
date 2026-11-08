window.BENCHMARK_DATA = {
  "lastUpdate": 1794131351564,
  "repoUrl": "https://github.com/jtulach/graalvm-native-libs",
  "entries": {
    "jvm-channel ratios": [
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
          "id": "379abeb43413a3cc48c211e772a0e0ed9d392f9a",
          "message": "Vue.js app to display the graphs",
          "timestamp": "2026-11-05T17:26:28Z",
          "url": "https://github.com/jtulach/graalvm-native-libs/commit/379abeb43413a3cc48c211e772a0e0ed9d392f9a"
        },
        "date": 1793901784614,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "jvm-channel: mock round trip",
            "value": 37,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: hotspot baseline",
            "value": 31,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: mock/hotspot ratio",
            "value": 1.1935483870967742,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native (NI+HotSpot) round trip",
            "value": 298,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: native/mock ratio",
            "value": 8.054054054054054,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native/hotspot ratio",
            "value": 9.612903225806452,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (invokeString)",
            "value": 63.909460304487894,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (arrayAccess)",
            "value": 121.38472129058884,
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
          "id": "c8ddf458d4d8695e9f9bfc01cce7ca46f3720cd2",
          "message": "Setting up GraalVM the most modern way",
          "timestamp": "2026-11-05T11:08:55Z",
          "url": "https://github.com/jtulach/graalvm-native-libs/commit/c8ddf458d4d8695e9f9bfc01cce7ca46f3720cd2"
        },
        "date": 1793959900771,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "jvm-channel: mock round trip",
            "value": 37,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: hotspot baseline",
            "value": 31,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: mock/hotspot ratio",
            "value": 1.1935483870967742,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native (NI+HotSpot) round trip",
            "value": 292,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: native/mock ratio",
            "value": 7.891891891891892,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native/hotspot ratio",
            "value": 9.419354838709678,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (invokeString)",
            "value": 62.63384735001697,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (arrayAccess)",
            "value": 115.09214151937566,
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
          "id": "c8ddf458d4d8695e9f9bfc01cce7ca46f3720cd2",
          "message": "Setting up GraalVM the most modern way",
          "timestamp": "2026-11-05T11:08:55Z",
          "url": "https://github.com/jtulach/graalvm-native-libs/commit/c8ddf458d4d8695e9f9bfc01cce7ca46f3720cd2"
        },
        "date": 1794043833130,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "jvm-channel: mock round trip",
            "value": 30,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: hotspot baseline",
            "value": 25,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: mock/hotspot ratio",
            "value": 1.2,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native (NI+HotSpot) round trip",
            "value": 216,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: native/mock ratio",
            "value": 7.2,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native/hotspot ratio",
            "value": 8.64,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (invokeString)",
            "value": 65.73211335314868,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (arrayAccess)",
            "value": 127.72810676831618,
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
          "id": "c8ddf458d4d8695e9f9bfc01cce7ca46f3720cd2",
          "message": "Setting up GraalVM the most modern way",
          "timestamp": "2026-11-05T11:08:55Z",
          "url": "https://github.com/jtulach/graalvm-native-libs/commit/c8ddf458d4d8695e9f9bfc01cce7ca46f3720cd2"
        },
        "date": 1794131350586,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "jvm-channel: mock round trip",
            "value": 35,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: hotspot baseline",
            "value": 26,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: mock/hotspot ratio",
            "value": 1.3461538461538463,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native (NI+HotSpot) round trip",
            "value": 216,
            "unit": "ns"
          },
          {
            "name": "jvm-channel: native/mock ratio",
            "value": 6.171428571428572,
            "unit": "x"
          },
          {
            "name": "jvm-channel: native/hotspot ratio",
            "value": 8.307692307692308,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (invokeString)",
            "value": 60.54499653951488,
            "unit": "x"
          },
          {
            "name": "jvm-interop: channel/local ratio (arrayAccess)",
            "value": 96.28125362550638,
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
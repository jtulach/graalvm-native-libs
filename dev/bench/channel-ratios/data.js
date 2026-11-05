window.BENCHMARK_DATA = {
  "lastUpdate": 1793901785283,
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
      }
    ]
  }
}
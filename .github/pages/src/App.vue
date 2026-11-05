<script setup lang="ts">
import { computed, ref } from 'vue';
import BenchmarkChart from './BenchmarkChart.vue';
import type { BenchmarkData, BenchmarkEntry, BenchmarkGroup } from './types';

interface BenchmarkSuite {
  name: string;
  entries: BenchmarkEntry[];
  groups: BenchmarkGroup[];
}

const benchmarkData = window.BENCHMARK_DATA;
const query = ref('');
const repositoryUrl = benchmarkData?.repoUrl || 'https://github.com/jtulach/graalvm-native-libs';
const error = ref('');

function parseBenchmarkName(name: string): { group: string; series: string; workload: string | null } {
  const colon = name.indexOf(': ');
  if (colon > 0) {
    return { group: name.slice(0, colon), series: name.slice(colon + 2), workload: null };
  }
  const parts = name.split('.');
  const method = parts.pop() || name;
  const className = parts.pop() || '';
  const match = /^[a-z]+([A-Z].*)$/.exec(method);
  return {
    group: className,
    series: className ? `${className}.${method}` : method,
    workload: match?.[1] || null,
  };
}

function groupBenchmarks(entries: BenchmarkEntry[]): BenchmarkGroup[] {
  const infos = new Map<string, ReturnType<typeof parseBenchmarkName> & { unit: string }>();
  for (const entry of entries) {
    for (const bench of entry.benches || []) {
      if (!infos.has(bench.name)) {
        infos.set(bench.name, { ...parseBenchmarkName(bench.name), unit: bench.unit });
      }
    }
  }

  const workloadCount = new Map<string, number>();
  for (const info of infos.values()) {
    if (info.workload) workloadCount.set(info.workload, (workloadCount.get(info.workload) || 0) + 1);
  }

  const groups = new Map<string, BenchmarkGroup>();
  for (const [name, info] of infos) {
    const title = info.workload && (workloadCount.get(info.workload) || 0) > 1 ? info.workload : info.group;
    const key = `${title} [${info.unit}]`;
    let group = groups.get(key);
    if (!group) {
      group = { title: key, unit: info.unit, series: [] };
      groups.set(key, group);
    }
    group.series.push({ name, label: info.series });
  }
  return [...groups.values()];
}

function isBenchmarkData(data: unknown): data is BenchmarkData {
  if (!data || typeof data !== 'object' || Array.isArray(data)) return false;
  return 'entries' in data
    && !!data.entries
    && typeof data.entries === 'object'
    && !Array.isArray(data.entries);
}

let suites: BenchmarkSuite[] = [];
if (isBenchmarkData(benchmarkData)) {
  suites = Object.entries(benchmarkData.entries).map(([name, value]) => {
    const entries = Array.isArray(value) ? value : [];
    return { name, entries, groups: groupBenchmarks(entries) };
  });
} else {
  error.value = 'The published benchmark data is missing or has an invalid format.';
}

const lastUpdated = computed(() => {
  if (!benchmarkData?.lastUpdate) return 'Unavailable';
  const date = new Date(benchmarkData.lastUpdate);
  return Number.isNaN(date.getTime()) ? 'Unavailable' : date.toLocaleString();
});

const totalSeries = computed(() => suites.reduce(
  (count, suite) => count + suite.groups.reduce((sum, group) => sum + group.series.length, 0),
  0,
));
const totalRuns = computed(() => suites.reduce((count, suite) => count + suite.entries.length, 0));

const filteredSuites = computed(() => {
  const needle = query.value.toLocaleLowerCase();
  return suites.map(suite => {
    const visibleGroups = suite.groups.filter(group => !needle
      || suite.name.toLocaleLowerCase().includes(needle)
      || group.title.toLocaleLowerCase().includes(needle)
      || group.series.some(series => series.label.toLocaleLowerCase().includes(needle)));
    return { ...suite, visibleGroups };
  }).filter(suite => suite.visibleGroups.length);
});

function downloadData(): void {
  if (!benchmarkData) return;
  const blob = new Blob([JSON.stringify(benchmarkData, null, 2)], { type: 'application/json' });
  const url = URL.createObjectURL(blob);
  const link = document.createElement('a');
  link.href = url;
  link.download = 'benchmark_data.json';
  link.click();
  link.remove();
  window.setTimeout(() => URL.revokeObjectURL(url), 0);
}
</script>

<template>
  <div>
    <header class="topbar">
      <a class="brand" href="https://jtulach.github.io/graalvm-native-libs/" aria-label="GraalVM Native Libraries home">
        <span class="brand-mark" aria-hidden="true">G</span>
        <span class="brand-name">GraalVM Native Libraries <span>BENCHMARKS</span></span>
      </a>
      <a class="repo-link" :href="repositoryUrl" target="_blank" rel="noopener noreferrer">
        View repository <span aria-hidden="true">↗</span>
      </a>
    </header>

    <main class="page-content">
      <section class="intro">
        <div class="intro-copy">
          <p class="eyebrow">Performance dashboard</p>
          <h1>Performance,<br /><span>over time.</span></h1>
          <p class="intro-description">Explore benchmark history across GraalVM Native Libraries. Select any point on a chart to view its commit.</p>
        </div>
        <div class="updated-card">
          <span class="status-dot" aria-hidden="true"></span>
          <div>
            <span class="updated-label">Last data update</span>
            <strong>{{ lastUpdated }}</strong>
          </div>
        </div>
      </section>

      <section v-if="error" class="message-card error-card" role="alert">
        <h2>Benchmark data unavailable</h2>
        <p>{{ error }}</p>
        <p class="message-hint">The dashboard expects the benchmark action to publish a <code>data.js</code> file beside this page.</p>
      </section>

      <template v-else>
        <section class="stats-grid" aria-label="Benchmark summary">
          <article class="stat-card">
            <span class="stat-icon" aria-hidden="true">▤</span>
            <span class="stat-label">Benchmark suites</span>
            <strong>{{ suites.length }}</strong>
          </article>
          <article class="stat-card">
            <span class="stat-icon purple" aria-hidden="true">⌁</span>
            <span class="stat-label">Tracked series</span>
            <strong>{{ totalSeries }}</strong>
          </article>
          <article class="stat-card">
            <span class="stat-icon green" aria-hidden="true">◷</span>
            <span class="stat-label">Recorded runs</span>
            <strong>{{ totalRuns }}</strong>
          </article>
        </section>

        <section class="benchmark-section">
          <div class="section-heading">
            <div>
              <p class="eyebrow">History</p>
              <h2>Benchmark results</h2>
            </div>
            <div class="toolbar">
              <label class="search-box">
                <span class="search-icon" aria-hidden="true">⌕</span>
                <input v-model.trim="query" type="search" placeholder="Filter benchmarks" aria-label="Filter benchmarks" />
              </label>
              <button class="download-button" type="button" @click="downloadData">
                <span aria-hidden="true">↓</span> Download JSON
              </button>
            </div>
          </div>

          <div v-if="!suites.length" class="message-card">
            <h2>No benchmark history yet</h2>
            <p>Results will appear here after the first benchmark run is published.</p>
          </div>
          <div v-else-if="!filteredSuites.length" class="message-card">
            <h2>No matching benchmarks</h2>
            <p>Try a different search term or clear the filter.</p>
          </div>
          <section v-for="suite in filteredSuites" :key="suite.name" class="suite-card">
            <div class="suite-heading">
              <div>
                <p class="eyebrow">Benchmark suite</p>
                <h3>{{ suite.name }}</h3>
              </div>
              <span class="run-count">{{ suite.entries.length }} {{ suite.entries.length === 1 ? 'run' : 'runs' }}</span>
            </div>
            <div class="charts-grid">
              <article v-for="group in suite.visibleGroups" :key="group.title" class="chart-card">
                <div class="chart-heading">
                  <h4>{{ group.title }}</h4>
                  <span class="unit-pill">{{ group.unit }}</span>
                </div>
                <BenchmarkChart :group="group" :entries="suite.entries" />
              </article>
            </div>
          </section>
        </section>
      </template>
    </main>

    <footer class="footer">
      <span>GraalVM Native Libraries</span>
      <span>Benchmark data powered by <a href="https://github.com/marketplace/actions/continuous-benchmark" target="_blank" rel="noopener noreferrer">github-action-benchmark</a></span>
    </footer>
  </div>
</template>

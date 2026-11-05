<script setup lang="ts">
import { onBeforeUnmount, onMounted, ref } from 'vue';
import Chart from 'chart.js/auto';
import type { ChartConfiguration } from 'chart.js';
import type { BenchmarkEntry, BenchmarkGroup } from './types';

const props = defineProps<{
  group: BenchmarkGroup;
  entries: BenchmarkEntry[];
}>();

const palette = ['#e45757', '#2a9d8f', '#5271d5', '#e89a3c', '#9b6bd3', '#3c9ac5', '#cf6b9b', '#788b44', '#bd7445', '#536878'];
const canvas = ref<HTMLCanvasElement>();
let chart: Chart | undefined;

onMounted(() => {
  const config: ChartConfiguration<'line'> = {
    type: 'line',
    data: {
      labels: props.entries.map(entry => (entry.commit?.id || '').slice(0, 7)),
      datasets: props.group.series.map((series, index) => {
        const color = palette[index % palette.length];
        return {
          label: series.label,
          data: props.entries.map(entry => entry.benches?.find(bench => bench.name === series.name)?.value ?? null),
          borderColor: color,
          backgroundColor: color,
          borderWidth: 2,
          pointRadius: 3,
          pointHoverRadius: 5,
          fill: false,
          spanGaps: true,
        };
      }),
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          position: 'bottom',
          labels: { usePointStyle: true, boxWidth: 8, padding: 18 },
        },
        tooltip: {
          callbacks: {
            afterTitle(items) {
              const commit = props.entries[items[0]?.dataIndex]?.commit;
              if (!commit) return '';
              const details = [commit.message, commit.timestamp];
              if (commit.committer?.username) details.push(`committed by @${commit.committer.username}`);
              return details.filter(Boolean).join('\n');
            },
            label(context) {
              return `${context.dataset.label}: ${context.formattedValue} ${props.group.unit}`;
            },
          },
        },
      },
      elements: { line: { tension: 0.25 } },
      scales: {
        x: {
          grid: { display: false },
          title: { display: true, text: 'Commit' },
        },
        y: {
          grid: { color: '#edf0f5' },
          title: { display: true, text: props.group.unit },
          beginAtZero: true,
        },
      },
      onClick(_event, activeElements) {
        const commit = props.entries[activeElements[0]?.index ?? -1]?.commit;
        if (commit?.url) window.open(commit.url, '_blank', 'noopener,noreferrer');
      },
    },
  };

  if (canvas.value) chart = new Chart(canvas.value, config);
});

onBeforeUnmount(() => chart?.destroy());
</script>

<template>
  <div class="chart-frame">
    <canvas ref="canvas" role="img" :aria-label="`${group.title} benchmark history`"></canvas>
  </div>
</template>

import type { BenchmarkData } from './types';

declare global {
  interface Window {
    BENCHMARK_DATA?: BenchmarkData;
  }
}

export {};

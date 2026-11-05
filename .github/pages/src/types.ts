export interface BenchmarkCommit {
  id?: string;
  message?: string;
  timestamp?: string;
  url?: string;
  committer?: {
    username?: string;
  };
}

export interface Benchmark {
  name: string;
  unit: string;
  value: number;
}

export interface BenchmarkEntry {
  commit?: BenchmarkCommit;
  benches?: Benchmark[];
}

export interface BenchmarkData {
  entries: Record<string, BenchmarkEntry[]>;
  repoUrl?: string;
  lastUpdate?: string | number;
}

export interface BenchmarkSeries {
  name: string;
  label: string;
}

export interface BenchmarkGroup {
  title: string;
  unit: string;
  series: BenchmarkSeries[];
}

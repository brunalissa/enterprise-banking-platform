import type { PrometheusMetrics } from '@/types/monitoring';

// Request statistics are averages since startup, not a rolling PromQL rate.
export function parsePrometheusMetrics(text: string): PrometheusMetrics {
  const samples = text.split('\n').flatMap((line) => {
    const match = line.match(/^([a-zA-Z_:][\w:]*)(\{.*\})?\s+([-+\d.eE]+)(?:\s+\d+)?$/);
    if (!match) return [];
    const value = Number(match[3]);
    return Number.isFinite(value) ? [{ name: match[1], labels: match[2] || '', value }] : [];
  });
  if (!samples.length) throw new Error('No numeric Prometheus samples received');
  const sum = (name: string, label?: RegExp) => samples
    .filter((sample) => sample.name === name && (!label || label.test(sample.labels)))
    .reduce((total, sample) => total + sample.value, 0);
  const count = sum('http_server_requests_seconds_count');
  const uptime = sum('process_uptime_seconds');
  const heapUsed = sum('jvm_memory_used_bytes', /area="heap"/);
  const heapMax = sum('jvm_memory_max_bytes', /area="heap"/);
  return {
    cpuUsage: sum('process_cpu_usage') * 100,
    memoryUsage: heapMax > 0 ? heapUsed / heapMax * 100 : 0,
    requestRate: uptime > 0 ? count / uptime : 0,
    errorRate: count > 0 ? sum('http_server_requests_seconds_count', /status="5\d\d"/) / count * 100 : 0,
    avgResponseTime: count > 0 ? sum('http_server_requests_seconds_sum') / count * 1000 : 0,
    dbConnections: sum('hikaricp_connections_active'),
    activeThreads: sum('jvm_threads_live_threads'),
  };
}

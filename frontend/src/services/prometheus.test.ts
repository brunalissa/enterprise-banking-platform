import { describe, expect, it } from 'vitest';
import { parsePrometheusMetrics } from './prometheus';

describe('Prometheus metrics adapter', () => {
  it('converts exposition text to dashboard units and aggregates labeled samples', () => {
    const metrics = parsePrometheusMetrics(`# HELP process_cpu_usage CPU
process_cpu_usage 0.25
process_uptime_seconds 10
jvm_memory_used_bytes{area="heap",id="eden"} 20
jvm_memory_used_bytes{area="heap",id="old"} 30
jvm_memory_max_bytes{area="heap",id="old"} 100
http_server_requests_seconds_count{status="200"} 18
http_server_requests_seconds_count{status="500"} 2
http_server_requests_seconds_sum{status="200"} 0.8
http_server_requests_seconds_sum{status="500"} 0.2
hikaricp_connections_active{pool="main"} 3
jvm_threads_live_threads 12`);
    expect(metrics).toEqual({ cpuUsage: 25, memoryUsage: 50, requestRate: 2,
      errorRate: 10, avgResponseTime: 50, dbConnections: 3, activeThreads: 12 });
  });
  it('avoids division by zero before any requests arrive', () => {
    const metrics = parsePrometheusMetrics('process_cpu_usage 0\nprocess_uptime_seconds 0');
    expect(Object.values(metrics).every(Number.isFinite)).toBe(true);
  });
  it('rejects invalid responses rather than displaying demo metrics', () => {
    expect(() => parsePrometheusMetrics('<html>unavailable</html>')).toThrow();
  });
});

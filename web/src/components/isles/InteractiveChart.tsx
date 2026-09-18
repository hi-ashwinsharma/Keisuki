import React, { useState, useRef, useEffect } from 'react';
import { playTactileTick } from './SoundEffectsController';
import { TrendingUp, Flame, BarChart3 } from 'lucide-react';

interface DataPoint {
  label: string;
  value: number;
  time: string;
}

const WEEK_DATA: DataPoint[] = [
  { label: 'Mon', value: 45, time: '09:30 AM' },
  { label: 'Tue', value: 72, time: '02:15 PM' },
  { label: 'Wed', value: 38, time: '11:00 AM' },
  { label: 'Thu', value: 110, time: '06:45 PM' },
  { label: 'Fri', value: 85, time: '04:20 PM' },
  { label: 'Sat', value: 140, time: '08:10 PM' },
  { label: 'Sun', value: 95, time: '05:30 PM' },
];

const MONTH_DATA: DataPoint[] = [
  { label: 'W1', value: 280, time: 'Week 1' },
  { label: 'W2', value: 410, time: 'Week 2' },
  { label: 'W3', value: 360, time: 'Week 3' },
  { label: 'W4', value: 520, time: 'Week 4' },
];

export default function InteractiveChart() {
  const [range, setRange] = useState<'week' | 'month'>('week');
  const [hoveredIndex, setHoveredIndex] = useState<number | null>(null);
  const canvasRef = useRef<HTMLCanvasElement | null>(null);

  const data = range === 'week' ? WEEK_DATA : MONTH_DATA;
  const activePoint = hoveredIndex !== null ? data[hoveredIndex] : data[data.length - 1];

  // Draw chart on canvas
  useEffect(() => {
    const canvas = canvasRef.current;
    if (!canvas) return;
    const ctx = canvas.getContext('2d');
    if (!ctx) return;

    // Handle high DPI
    const dpr = window.devicePixelRatio || 1;
    const rect = canvas.getBoundingClientRect();
    canvas.width = rect.width * dpr;
    canvas.height = rect.height * dpr;
    ctx.scale(dpr, dpr);

    const width = rect.width;
    const height = rect.height;
    ctx.clearRect(0, 0, width, height);

    const paddingX = 24;
    const paddingBottom = 32;
    const paddingTop = 20;
    const chartHeight = height - paddingBottom - paddingTop;
    const chartWidth = width - paddingX * 2;

    const maxValue = Math.max(...data.map((d) => d.value)) * 1.2;
    const barWidth = Math.min(36, chartWidth / data.length - 12);
    const stepX = chartWidth / (data.length - 1 || 1);

    // Draw grid lines
    ctx.strokeStyle = 'rgba(255, 255, 255, 0.04)';
    ctx.lineWidth = 1;
    for (let i = 0; i <= 3; i++) {
      const y = paddingTop + (chartHeight / 3) * i;
      ctx.beginPath();
      ctx.moveTo(paddingX, y);
      ctx.lineTo(width - paddingX, y);
      ctx.stroke();
    }

    // Draw Bars with rounded corners
    data.forEach((point, i) => {
      const isSelected = hoveredIndex === i || (hoveredIndex === null && i === data.length - 1);
      const barHeight = (point.value / maxValue) * chartHeight;
      const x = paddingX + i * stepX - barWidth / 2;
      const y = height - paddingBottom - barHeight;

      // Solid color fill
      ctx.fillStyle = isSelected ? '#FF2E63' : 'rgba(255, 46, 99, 0.35)';
      ctx.beginPath();
      const radius = 6;
      ctx.roundRect(x, y, barWidth, barHeight, [radius, radius, 2, 2]);
      ctx.fill();

      // Bar Top Glow on active bar
      if (isSelected) {
        ctx.shadowColor = '#FF2E63';
        ctx.shadowBlur = 12;
        ctx.fillStyle = '#FFF8F0';
        ctx.beginPath();
        ctx.arc(x + barWidth / 2, y + 4, 3, 0, Math.PI * 2);
        ctx.fill();
        ctx.shadowBlur = 0;
      }

      // X-Axis Labels
      ctx.fillStyle = isSelected ? '#FFF8F0' : 'rgba(255, 255, 255, 0.4)';
      ctx.font = isSelected ? '600 12px "Plus Jakarta Sans"' : '400 11px "Plus Jakarta Sans"';
      ctx.textAlign = 'center';
      ctx.fillText(point.label, paddingX + i * stepX, height - 10);
    });
  }, [data, hoveredIndex, range]);

  const handleMouseMove = (e: React.MouseEvent<HTMLCanvasElement>) => {
    const canvas = canvasRef.current;
    if (!canvas) return;
    const rect = canvas.getBoundingClientRect();
    const mouseX = e.clientX - rect.left;
    const paddingX = 24;
    const chartWidth = rect.width - paddingX * 2;
    const stepX = chartWidth / (data.length - 1 || 1);

    const index = Math.round((mouseX - paddingX) / stepX);
    if (index >= 0 && index < data.length && index !== hoveredIndex) {
      setHoveredIndex(index);
      playTactileTick(650);
    }
  };

  return (
    <div className="w-full glass-panel rounded-3xl p-6 border border-white/10 relative overflow-hidden bg-obsidian-900/80">
      {/* Top Bar / Range switcher */}
      <div className="flex items-center justify-between mb-6 pb-4 border-b border-white/5">
        <div className="flex items-center gap-3">
          <div className="p-2.5 rounded-xl bg-ruby-500/10 border border-ruby-500/20 text-ruby-400">
            <BarChart3 className="w-4 h-4" />
          </div>
          <div>
            <h3 className="text-sm font-semibold text-white">Interactive Canvas Telemetry</h3>
            <p className="text-xs text-zinc-400">Scrub across bars to inspect count frequency</p>
          </div>
        </div>

        <div className="flex items-center gap-1 bg-obsidian-950 p-1 rounded-xl border border-white/5">
          <button
            onClick={() => {
              playTactileTick(500);
              setRange('week');
              setHoveredIndex(null);
            }}
            className={`px-3 py-1 rounded-lg text-xs font-medium transition-all ${
              range === 'week' ? 'bg-ruby-500 text-white shadow-sm' : 'text-zinc-400 hover:text-white'
            }`}
          >
            Week
          </button>
          <button
            onClick={() => {
              playTactileTick(500);
              setRange('month');
              setHoveredIndex(null);
            }}
            className={`px-3 py-1 rounded-lg text-xs font-medium transition-all ${
              range === 'month' ? 'bg-ruby-500 text-white shadow-sm' : 'text-zinc-400 hover:text-white'
            }`}
          >
            Month
          </button>
        </div>
      </div>

      {/* Live Scrubber Inspector Pill */}
      <div className="flex items-center justify-between bg-obsidian-950/80 p-3.5 rounded-2xl border border-white/5 mb-4">
        <div className="flex items-center gap-2">
          <span className="w-2 h-2 rounded-full bg-ruby-400 animate-ping" />
          <span className="text-xs text-zinc-400 font-medium">{activePoint.label} • {activePoint.time}</span>
        </div>
        <div className="text-right">
          <span className="text-xl font-bold num-mono text-amber-400">+{activePoint.value}</span>
          <span className="text-xs text-zinc-500 ml-1.5">counts</span>
        </div>
      </div>

      {/* Canvas Drawing Surface */}
      <div className="relative h-48 w-full cursor-crosshair">
        <canvas
          ref={canvasRef}
          onMouseMove={handleMouseMove}
          onMouseLeave={() => setHoveredIndex(null)}
          className="w-full h-full block"
        />
      </div>

      {/* Summary Bento Stats */}
      <div className="grid grid-cols-3 gap-3 mt-4 pt-4 border-t border-white/5">
        <div className="p-3 rounded-2xl bg-white/[0.02] border border-white/5 text-center">
          <span className="text-[10px] uppercase text-zinc-500 font-bold block mb-1">Total Velocity</span>
          <span className="text-base font-bold num-mono text-white">585 / wk</span>
        </div>
        <div className="p-3 rounded-2xl bg-white/[0.02] border border-white/5 text-center">
          <span className="text-[10px] uppercase text-zinc-500 font-bold block mb-1 flex items-center justify-center gap-1">
            <Flame className="w-3 h-3 text-amber-400" /> Active Streak
          </span>
          <span className="text-base font-bold num-mono text-amber-400">14 Days</span>
        </div>
        <div className="p-3 rounded-2xl bg-white/[0.02] border border-white/5 text-center">
          <span className="text-[10px] uppercase text-zinc-500 font-bold block mb-1 flex items-center justify-center gap-1">
            <TrendingUp className="w-3 h-3 text-emerald-400" /> Daily Avg
          </span>
          <span className="text-base font-bold num-mono text-emerald-400">83.5</span>
        </div>
      </div>
    </div>
  );
}

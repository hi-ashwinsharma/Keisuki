import React, { useState, useRef, useEffect } from 'react';
import { playTactileTick } from './SoundEffectsController';
import { TrendingUp, Flame, Wifi, Battery } from 'lucide-react';

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

    const paddingX = 20;
    const paddingBottom = 28;
    const paddingTop = 16;
    const chartHeight = height - paddingBottom - paddingTop;
    const chartWidth = width - paddingX * 2;

    const maxValue = Math.max(...data.map((d) => d.value)) * 1.25;
    const barWidth = Math.min(32, chartWidth / data.length - 10);
    const stepX = chartWidth / (data.length - 1 || 1);

    // Draw horizontal grid lines
    ctx.strokeStyle = 'rgba(255, 255, 255, 0.04)';
    ctx.lineWidth = 1;
    for (let i = 0; i <= 3; i++) {
      const y = paddingTop + (chartHeight / 3) * i;
      ctx.beginPath();
      ctx.moveTo(paddingX, y);
      ctx.lineTo(width - paddingX, y);
      ctx.stroke();
    }

    // Draw bars
    data.forEach((d, i) => {
      const x = paddingX + i * (chartWidth / (data.length - 1 || 1)) - barWidth / 2;
      const barH = (d.value / maxValue) * chartHeight;
      const y = paddingTop + chartHeight - barH;
      const isHovered = hoveredIndex === i;

      // Solid fill
      ctx.fillStyle = isHovered ? '#FF9F1C' : 'rgba(255, 159, 28, 0.25)';
      ctx.beginPath();
      ctx.roundRect(x, y, barWidth, barH, [4, 4, 0, 0]);
      ctx.fill();

      // Bottom label
      ctx.fillStyle = isHovered ? '#FFF8F0' : 'rgba(255, 248, 240, 0.4)';
      ctx.font = '10px JetBrains Mono, monospace';
      ctx.textAlign = 'center';
      ctx.fillText(d.label, x + barWidth / 2, height - 8);
    });

    // Draw line overlay
    ctx.strokeStyle = '#FF2E63';
    ctx.lineWidth = 2.5;
    ctx.beginPath();
    data.forEach((d, i) => {
      const x = paddingX + i * stepX;
      const y = paddingTop + chartHeight - (d.value / maxValue) * chartHeight;
      if (i === 0) ctx.moveTo(x, y);
      else ctx.lineTo(x, y);
    });
    ctx.stroke();

    // Draw points on line
    data.forEach((d, i) => {
      const x = paddingX + i * stepX;
      const y = paddingTop + chartHeight - (d.value / maxValue) * chartHeight;
      const isHovered = hoveredIndex === i;

      ctx.fillStyle = isHovered ? '#FF2E63' : '#0A0A0C';
      ctx.strokeStyle = '#FF2E63';
      ctx.lineWidth = 2;
      ctx.beginPath();
      ctx.arc(x, y, isHovered ? 5 : 3.5, 0, Math.PI * 2);
      ctx.fill();
      ctx.stroke();
    });
  }, [data, hoveredIndex]);

  const handleMouseMove = (e: React.MouseEvent<HTMLCanvasElement>) => {
    const canvas = canvasRef.current;
    if (!canvas) return;
    const rect = canvas.getBoundingClientRect();
    const x = e.clientX - rect.left;
    const paddingX = 20;
    const chartWidth = rect.width - paddingX * 2;
    const stepX = chartWidth / (data.length - 1 || 1);

    const relativeX = x - paddingX;
    const index = Math.round(relativeX / stepX);

    if (index >= 0 && index < data.length && index !== hoveredIndex) {
      setHoveredIndex(index);
      playTactileTick(450 + index * 40);
    }
  };

  return (
    <div className="w-full max-w-sm sm:max-w-md mx-auto">
      {/* Phone Outer Bezel Chassis */}
      <div className="relative rounded-[44px] p-3 bg-obsidian-950 border border-white/20 shadow-[0_30px_70px_-15px_rgba(0,0,0,0.95)]">
        
        {/* Device Screen Surface */}
        <div className="relative rounded-[36px] p-5 sm:p-6 bg-obsidian-900 border border-white/5 overflow-hidden">
          
          {/* Wireframe Status Bar */}
          <div className="flex items-center justify-between pb-4 border-b border-white/5 text-[11px] num-mono text-zinc-400">
            <span>09:41</span>
            <div className="flex items-center gap-2">
              <Wifi className="w-3.5 h-3.5" />
              <Battery className="w-3.5 h-3.5" />
            </div>
          </div>

          {/* App Header & Range Filters */}
          <div className="flex items-center justify-between py-3.5 border-b border-white/5 mb-3">
            <div className="flex items-center gap-2">
              <span className="w-2 h-2 rounded-full bg-amber-400" />
              <span className="text-xs font-bold tracking-wider text-white">HABIT TELEMETRY</span>
            </div>

            <div className="flex items-center bg-obsidian-950 p-0.5 rounded-xl border border-white/10">
              <button
                type="button"
                onClick={() => {
                  playTactileTick(500);
                  setRange('week');
                  setHoveredIndex(null);
                }}
                className={`px-2.5 py-1 rounded-lg text-xs font-medium transition-all ${
                  range === 'week' ? 'bg-ruby-500 text-white shadow-sm font-semibold' : 'text-zinc-400 hover:text-white'
                }`}
              >
                Week
              </button>
              <button
                type="button"
                onClick={() => {
                  playTactileTick(500);
                  setRange('month');
                  setHoveredIndex(null);
                }}
                className={`px-2.5 py-1 rounded-lg text-xs font-medium transition-all ${
                  range === 'month' ? 'bg-ruby-500 text-white shadow-sm font-semibold' : 'text-zinc-400 hover:text-white'
                }`}
              >
                Month
              </button>
            </div>
          </div>

          {/* Live Scrubber Inspector Pill */}
          <div className="flex items-center justify-between bg-obsidian-950 p-3 rounded-2xl border border-white/5 mb-3">
            <div className="flex items-center gap-2">
              <span className="w-2 h-2 rounded-full bg-ruby-400 animate-pulse" />
              <span className="text-xs text-zinc-300 font-medium">{activePoint.label} • {activePoint.time}</span>
            </div>
            <div className="text-right">
              <span className="text-lg font-bold num-mono text-amber-400">+{activePoint.value}</span>
              <span className="text-[10px] text-zinc-500 ml-1">counts</span>
            </div>
          </div>

          {/* Canvas Drawing Surface */}
          <div className="relative h-44 w-full cursor-crosshair bg-obsidian-950/60 rounded-2xl border border-white/5 p-1 mb-3">
            <canvas
              ref={canvasRef}
              onMouseMove={handleMouseMove}
              onMouseLeave={() => setHoveredIndex(null)}
              className="w-full h-full block"
            />
          </div>

          {/* Summary Stats */}
          <div className="grid grid-cols-3 gap-2 pt-2 border-t border-white/5">
            <div className="p-2.5 rounded-xl bg-white/[0.02] border border-white/5 text-center">
              <span className="text-[9px] uppercase text-zinc-500 font-bold block mb-0.5">Velocity</span>
              <span className="text-xs font-bold num-mono text-white">585 / wk</span>
            </div>
            <div className="p-2.5 rounded-xl bg-white/[0.02] border border-white/5 text-center">
              <span className="text-[9px] uppercase text-zinc-500 font-bold block mb-0.5 flex items-center justify-center gap-1">
                <Flame className="w-2.5 h-2.5 text-amber-400" /> Streak
              </span>
              <span className="text-xs font-bold num-mono text-amber-400">14 Days</span>
            </div>
            <div className="p-2.5 rounded-xl bg-white/[0.02] border border-white/5 text-center">
              <span className="text-[9px] uppercase text-zinc-500 font-bold block mb-0.5 flex items-center justify-center gap-1">
                <TrendingUp className="w-2.5 h-2.5 text-emerald-400" /> Daily Avg
              </span>
              <span className="text-xs font-bold num-mono text-emerald-400">83.5</span>
            </div>
          </div>

        </div>
      </div>
    </div>
  );
}

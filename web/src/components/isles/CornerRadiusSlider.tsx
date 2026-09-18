import React, { useState } from 'react';
import { playTactileTick, playHeavyPulse } from './SoundEffectsController';
import { Sliders, Moon, Sparkles, Plus, RotateCcw, Check, Flame, ShieldCheck } from 'lucide-react';

export default function CornerRadiusSlider() {
  const [radius, setRadius] = useState<number>(20);
  const [isAmoled, setIsAmoled] = useState<boolean>(true);
  const [accent, setAccent] = useState<'ruby' | 'amber' | 'ivory'>('ruby');
  const [activeCount, setActiveCount] = useState<number>(108);

  const handleRadiusChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const val = parseInt(e.target.value, 10);
    if (Math.abs(val - radius) >= 2) {
      playTactileTick(380 + val * 12);
    }
    setRadius(val);
  };

  const handleAmoledToggle = () => {
    playHeavyPulse();
    setIsAmoled(!isAmoled);
  };

  const handleAccentChange = (newAccent: 'ruby' | 'amber' | 'ivory') => {
    playTactileTick(520);
    setAccent(newAccent);
  };

  const handleIncrement = () => {
    playTactileTick(680);
    setActiveCount((c) => c + 1);
  };

  const handleReset = () => {
    playHeavyPulse();
    setActiveCount(108);
  };

  const accentColor = accent === 'ruby' ? '#FF2E63' : accent === 'amber' ? '#FF9F1C' : '#FFF8F0';
  const accentTextClass = accent === 'ruby' ? 'text-ruby-400' : accent === 'amber' ? 'text-amber-400' : 'text-ivory-100';
  const accentBgClass = accent === 'ruby' ? 'bg-ruby-500' : accent === 'amber' ? 'bg-amber-500' : 'bg-ivory-200';
  const accentBorderClass = accent === 'ruby' ? 'border-ruby-500/30' : accent === 'amber' ? 'border-amber-500/30' : 'border-ivory-200/30';

  return (
    <div className="w-full glass-panel rounded-3xl p-6 sm:p-7 border border-white/10 bg-obsidian-900 shadow-2xl flex flex-col justify-between">
      
      {/* Top Header / Spec Bar */}
      <div className="flex items-center justify-between pb-4 border-b border-white/10 mb-5">
        <div className="flex items-center gap-2">
          <Sliders className="w-4 h-4 text-amber-400" />
          <span className="text-xs uppercase tracking-wider text-zinc-400 font-semibold">Surface & Theme Studio</span>
        </div>
        <div className="flex items-center gap-3">
          <span className="text-xs num-mono font-bold text-amber-400">
            {radius}dp Radius
          </span>
          <span className="text-[11px] num-mono px-2 py-0.5 rounded bg-white/5 border border-white/10 text-zinc-400">
            {isAmoled ? 'AMOLED 0% Pwr' : 'Obsidian'}
          </span>
        </div>
      </div>

      {/* Interactive App Mockup Preview Container */}
      <div
        className={`w-full p-4 sm:p-5 transition-colors duration-300 border ${
          isAmoled ? 'bg-black border-zinc-800 shadow-inner' : 'bg-obsidian-950 border-white/10'
        }`}
        style={{ borderRadius: `${Math.min(32, Math.max(16, radius + 8))}px` }}
      >
        {/* Mock Status Header */}
        <div className="flex items-center justify-between pb-3 mb-4 border-b border-white/5 text-[11px] text-zinc-500">
          <span className="num-mono font-bold text-zinc-400">09:41</span>
          <span className="tracking-wider uppercase text-[10px] text-zinc-500">Keisuki Engine</span>
          <div className="flex items-center gap-1.5">
            <span className="w-1.5 h-1.5 rounded-full bg-emerald-500" />
            <span className="num-mono text-[10px] text-zinc-400">100%</span>
          </div>
        </div>

        {/* Primary Interactive Counter Card */}
        <div
          className={`p-4 mb-3 border transition-all duration-200 ${
            isAmoled ? 'bg-zinc-950/90 border-zinc-800' : 'bg-obsidian-900 border-white/10'
          }`}
          style={{ borderRadius: `${radius}px` }}
        >
          <div className="flex justify-between items-start mb-2">
            <div>
              <span className="text-xs font-semibold text-zinc-300 block">Mindful Meditation</span>
              <span className="text-[11px] text-zinc-500 font-normal">Daily conscious repetitions</span>
            </div>
            <span
              className="w-3 h-3 transition-colors"
              style={{
                backgroundColor: accentColor,
                borderRadius: `${Math.max(2, radius * 0.4)}px`
              }}
            />
          </div>

          <div className="flex items-baseline justify-between my-2">
            <span className="text-4xl sm:text-5xl font-extrabold num-mono text-white tracking-tight">
              {activeCount}
            </span>
            <div className="flex items-center gap-1.5">
              <button
                type="button"
                onClick={handleReset}
                title="Reset Counter"
                className="p-2 bg-white/5 hover:bg-white/10 text-zinc-400 hover:text-white transition-all active:scale-95"
                style={{ borderRadius: `${Math.max(4, radius * 0.35)}px` }}
              >
                <RotateCcw className="w-3.5 h-3.5" />
              </button>
              <button
                type="button"
                onClick={handleIncrement}
                className="flex items-center gap-1.5 px-3.5 py-2 text-white font-bold text-xs transition-all active:scale-95 shadow-md"
                style={{
                  backgroundColor: accentColor,
                  borderRadius: `${Math.max(4, radius * 0.4)}px`,
                  color: accent === 'ivory' ? '#0A0A0C' : '#FFF8F0'
                }}
              >
                <Plus className="w-3.5 h-3.5" />
                <span>Tap</span>
              </button>
            </div>
          </div>
        </div>

        {/* Secondary Habit Metric Card */}
        <div
          className={`p-3.5 mb-3 border transition-all duration-200 flex items-center justify-between ${
            isAmoled ? 'bg-zinc-950/90 border-zinc-800' : 'bg-obsidian-900 border-white/10'
          }`}
          style={{ borderRadius: `${Math.max(6, radius * 0.85)}px` }}
        >
          <div className="flex items-center gap-3">
            <div
              className="p-2 bg-white/5 border border-white/5 text-zinc-400"
              style={{ borderRadius: `${Math.max(4, radius * 0.4)}px` }}
            >
              <Flame className="w-4 h-4 text-amber-400" />
            </div>
            <div>
              <span className="text-xs font-semibold text-zinc-200 block">Deep Focus Sprint</span>
              <span className="text-[10px] text-zinc-500 num-mono">4 of 4 intervals met</span>
            </div>
          </div>

          <div className="flex items-center gap-2">
            <div
              className="w-16 h-1.5 bg-white/10 overflow-hidden"
              style={{ borderRadius: `${Math.max(2, radius * 0.25)}px` }}
            >
              <div
                className="h-full transition-all duration-300"
                style={{
                  width: '100%',
                  backgroundColor: accentColor
                }}
              />
            </div>
            <Check className="w-3.5 h-3.5 text-emerald-400" />
          </div>
        </div>

        {/* Dynamic Navigation Pill / Action Bar */}
        <div
          className={`px-4 py-2.5 border flex items-center justify-between text-[11px] text-zinc-400 ${
            isAmoled ? 'bg-zinc-900/60 border-zinc-800' : 'bg-obsidian-900/80 border-white/5'
          }`}
          style={{ borderRadius: `${Math.max(8, radius)}px` }}
        >
          <span className="font-semibold text-white">Daily View</span>
          <span className="num-mono text-zinc-500">2 Active Tracks</span>
          <span className="text-emerald-400 font-medium">100% Goal</span>
        </div>

      </div>

      {/* Interactive Adjusters & AMOLED Controls Toolbar */}
      <div className="mt-5 pt-4 border-t border-white/10 space-y-4">
        
        {/* Radius Slider Control */}
        <div>
          <div className="flex justify-between items-center text-[11px] text-zinc-400 mb-1.5 font-medium">
            <span>Corner Curvature:</span>
            <div className="flex gap-4 num-mono text-[10px] text-zinc-500">
              <span className={radius <= 4 ? 'text-amber-400 font-bold' : ''}>0dp Sharp</span>
              <span className={radius >= 14 && radius <= 22 ? 'text-amber-400 font-bold' : ''}>16dp Balanced</span>
              <span className={radius >= 28 ? 'text-amber-400 font-bold' : ''}>32dp Soft</span>
            </div>
          </div>
          <input
            type="range"
            min="0"
            max="32"
            value={radius}
            onChange={handleRadiusChange}
            className="w-full accent-amber-400 bg-obsidian-950 h-2 rounded-lg cursor-pointer"
          />
        </div>

        {/* AMOLED Toggle & Color Accent Palette Switcher */}
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 pt-1">
          
          {/* AMOLED Setting Switch */}
          <button
            type="button"
            onClick={handleAmoledToggle}
            className={`flex items-center justify-between p-2.5 rounded-xl border transition-all text-left ${
              isAmoled
                ? 'bg-black border-amber-500/40 text-white'
                : 'bg-white/5 border-white/10 text-zinc-400 hover:text-white'
            }`}
          >
            <div className="flex items-center gap-2">
              <Moon className={`w-3.5 h-3.5 ${isAmoled ? 'text-amber-400' : 'text-zinc-400'}`} />
              <div>
                <span className="text-xs font-semibold block leading-none">AMOLED Mode</span>
                <span className="text-[10px] text-zinc-500 leading-none">True pitch black (#000)</span>
              </div>
            </div>
            <div
              className={`w-7 h-4 rounded-full p-0.5 transition-colors ${
                isAmoled ? 'bg-amber-500' : 'bg-zinc-700'
              }`}
            >
              <div
                className={`w-3 h-3 rounded-full bg-white transition-transform ${
                  isAmoled ? 'translate-x-3' : 'translate-x-0'
                }`}
              />
            </div>
          </button>

          {/* Accent Color Tokens */}
          <div className="flex items-center justify-between p-2.5 rounded-xl bg-white/5 border border-white/10">
            <span className="text-xs font-medium text-zinc-400">Accent Tint:</span>
            <div className="flex items-center gap-2">
              <button
                type="button"
                onClick={() => handleAccentChange('ruby')}
                title="Crimson Ruby"
                className={`w-5 h-5 rounded-full bg-ruby-500 transition-transform ${
                  accent === 'ruby' ? 'scale-125 ring-2 ring-white ring-offset-2 ring-offset-obsidian-900' : 'opacity-70 hover:opacity-100'
                }`}
              />
              <button
                type="button"
                onClick={() => handleAccentChange('amber')}
                title="Sunset Amber"
                className={`w-5 h-5 rounded-full bg-amber-500 transition-transform ${
                  accent === 'amber' ? 'scale-125 ring-2 ring-white ring-offset-2 ring-offset-obsidian-900' : 'opacity-70 hover:opacity-100'
                }`}
              />
              <button
                type="button"
                onClick={() => handleAccentChange('ivory')}
                title="Warm Ivory"
                className={`w-5 h-5 rounded-full bg-ivory-100 transition-transform ${
                  accent === 'ivory' ? 'scale-125 ring-2 ring-white ring-offset-2 ring-offset-obsidian-900' : 'opacity-70 hover:opacity-100'
                }`}
              />
            </div>
          </div>

        </div>

      </div>

    </div>
  );
}

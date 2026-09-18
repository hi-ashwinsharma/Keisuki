import React, { useState } from 'react';
import { playTactileTick, playHeavyPulse } from './SoundEffectsController';
import { Sliders, Moon, Plus, RotateCcw, Check, Flame, Wifi, Battery } from 'lucide-react';

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

  return (
    <div className="w-full max-w-sm sm:max-w-md mx-auto">
      {/* Phone Outer Bezel Chassis */}
      <div className="relative rounded-[44px] p-3 bg-obsidian-950 border border-white/20 shadow-[0_30px_70px_-15px_rgba(0,0,0,0.95)]">
        
        {/* Device Screen Surface */}
        <div
          className={`relative rounded-[36px] p-5 sm:p-6 transition-colors duration-300 border overflow-hidden ${
            isAmoled ? 'bg-black border-zinc-800' : 'bg-obsidian-900 border-white/5'
          }`}
        >
          {/* Status Bar */}
          <div className="flex items-center justify-between pb-3.5 border-b border-white/5 text-[11px] num-mono text-zinc-400">
            <span>09:41</span>
            <div className="flex items-center gap-2">
              <Wifi className="w-3.5 h-3.5" />
              <Battery className="w-3.5 h-3.5" />
            </div>
          </div>

          {/* App Top Header */}
          <div className="flex items-center justify-between py-3 border-b border-white/5 mb-3">
            <div className="flex items-center gap-2">
              <Sliders className="w-4 h-4 text-amber-400" />
              <span className="text-xs font-bold tracking-wider text-white">SURFACE STUDIO</span>
            </div>
            <span className="text-xs num-mono font-bold text-amber-400">
              {radius}dp
            </span>
          </div>

          {/* Primary Interactive Counter Card */}
          <div
            className={`p-4 mb-3 border transition-all duration-200 ${
              isAmoled ? 'bg-zinc-950 border-zinc-800' : 'bg-obsidian-950 border-white/10'
            }`}
            style={{ borderRadius: `${radius}px` }}
          >
            <div className="flex justify-between items-start mb-2">
              <div>
                <span className="text-xs font-semibold text-zinc-200 block">Mindful Repetitions</span>
                <span className="text-[10px] text-zinc-500">Live curvature scaling</span>
              </div>
              <span
                className="w-3 h-3 transition-colors"
                style={{
                  backgroundColor: accentColor,
                  borderRadius: `${Math.max(2, radius * 0.4)}px`,
                }}
              />
            </div>

            <div className="flex items-baseline justify-between my-2">
              <span className="text-4xl font-extrabold num-mono text-white tracking-tight">
                {activeCount}
              </span>
              <div className="flex items-center gap-1.5">
                <button
                  type="button"
                  onClick={handleReset}
                  title="Reset Counter"
                  className="p-1.5 bg-white/5 hover:bg-white/10 text-zinc-400 hover:text-white transition-all active:scale-95"
                  style={{ borderRadius: `${Math.max(4, radius * 0.35)}px` }}
                >
                  <RotateCcw className="w-3.5 h-3.5" />
                </button>
                <button
                  type="button"
                  onClick={handleIncrement}
                  className="flex items-center gap-1.5 px-3 py-1.5 text-white font-bold text-xs transition-all active:scale-95 shadow-md"
                  style={{
                    backgroundColor: accentColor,
                    borderRadius: `${Math.max(4, radius * 0.4)}px`,
                    color: accent === 'ivory' ? '#0A0A0C' : '#FFF8F0',
                  }}
                >
                  <Plus className="w-3.5 h-3.5" />
                  <span>Tap</span>
                </button>
              </div>
            </div>
          </div>

          {/* Secondary Metric Card */}
          <div
            className={`p-3 mb-3.5 border transition-all duration-200 flex items-center justify-between ${
              isAmoled ? 'bg-zinc-950 border-zinc-800' : 'bg-obsidian-950 border-white/10'
            }`}
            style={{ borderRadius: `${Math.max(6, radius * 0.85)}px` }}
          >
            <div className="flex items-center gap-2.5">
              <div
                className="p-1.5 bg-white/5 text-zinc-400"
                style={{ borderRadius: `${Math.max(4, radius * 0.4)}px` }}
              >
                <Flame className="w-3.5 h-3.5 text-amber-400" />
              </div>
              <div>
                <span className="text-xs font-semibold text-zinc-200 block">Focus Streak</span>
                <span className="text-[10px] text-zinc-500 num-mono">14 Days Clean</span>
              </div>
            </div>

            <div className="flex items-center gap-2">
              <div
                className="w-14 h-1.5 bg-white/10 overflow-hidden"
                style={{ borderRadius: `${Math.max(2, radius * 0.25)}px` }}
              >
                <div
                  className="h-full transition-all duration-300"
                  style={{
                    width: '100%',
                    backgroundColor: accentColor,
                  }}
                />
              </div>
              <Check className="w-3.5 h-3.5 text-emerald-400" />
            </div>
          </div>

          {/* Interactive Controls Panel */}
          <div className="pt-3 border-t border-white/5 space-y-3">
            {/* Radius Slider */}
            <div>
              <div className="flex justify-between items-center text-[11px] text-zinc-400 mb-1">
                <span>Corner Curvature</span>
                <span className="num-mono text-[10px] text-amber-400">{radius}dp</span>
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

            {/* AMOLED & Accent Controls */}
            <div className="grid grid-cols-2 gap-2 pt-1">
              <button
                type="button"
                onClick={handleAmoledToggle}
                className={`flex items-center justify-between p-2 rounded-xl border transition-all ${
                  isAmoled
                    ? 'bg-zinc-900 border-amber-500/40 text-white'
                    : 'bg-white/5 border-white/10 text-zinc-400 hover:text-white'
                }`}
              >
                <div className="flex items-center gap-1.5">
                  <Moon className={`w-3 h-3 ${isAmoled ? 'text-amber-400' : 'text-zinc-400'}`} />
                  <span className="text-[11px] font-semibold">AMOLED</span>
                </div>
                <div
                  className={`w-6 h-3.5 rounded-full p-0.5 transition-colors ${
                    isAmoled ? 'bg-amber-500' : 'bg-zinc-700'
                  }`}
                >
                  <div
                    className={`w-2.5 h-2.5 rounded-full bg-white transition-transform ${
                      isAmoled ? 'translate-x-2.5' : 'translate-x-0'
                    }`}
                  />
                </div>
              </button>

              <div className="flex items-center justify-between p-2 rounded-xl bg-white/5 border border-white/10">
                <span className="text-[11px] font-medium text-zinc-400">Tint</span>
                <div className="flex items-center gap-1.5">
                  <button
                    type="button"
                    onClick={() => handleAccentChange('ruby')}
                    title="Crimson Ruby"
                    className={`w-4 h-4 rounded-full bg-ruby-500 transition-transform ${
                      accent === 'ruby' ? 'scale-125 ring-2 ring-white' : 'opacity-70 hover:opacity-100'
                    }`}
                  />
                  <button
                    type="button"
                    onClick={() => handleAccentChange('amber')}
                    title="Sunset Amber"
                    className={`w-4 h-4 rounded-full bg-amber-500 transition-transform ${
                      accent === 'amber' ? 'scale-125 ring-2 ring-white' : 'opacity-70 hover:opacity-100'
                    }`}
                  />
                  <button
                    type="button"
                    onClick={() => handleAccentChange('ivory')}
                    title="Warm Ivory"
                    className={`w-4 h-4 rounded-full bg-ivory-100 transition-transform ${
                      accent === 'ivory' ? 'scale-125 ring-2 ring-white' : 'opacity-70 hover:opacity-100'
                    }`}
                  />
                </div>
              </div>
            </div>
          </div>

        </div>
      </div>
    </div>
  );
}

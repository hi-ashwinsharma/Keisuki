import React, { useState } from 'react';
import { playTactileTick } from './SoundEffectsController';
import { Sliders } from 'lucide-react';

export default function CornerRadiusSlider() {
  const [radius, setRadius] = useState<number>(24);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const val = parseInt(e.target.value, 10);
    if (Math.abs(val - radius) >= 2) {
      playTactileTick(400 + val * 10);
    }
    setRadius(val);
  };

  return (
    <div className="w-full glass-panel rounded-3xl p-6 border border-white/10 bg-obsidian-900">
      <div className="flex items-center justify-between mb-4">
        <div className="flex items-center gap-2">
          <Sliders className="w-4 h-4 text-amber-400" />
          <span className="text-xs uppercase tracking-wider text-zinc-400 font-semibold">Expressive Shapes</span>
        </div>
        <span className="text-xs num-mono font-bold text-amber-400">
          {radius}dp Radius
        </span>
      </div>

      <p className="text-xs text-zinc-400 mb-5 leading-relaxed">
        Custom dynamic curve algorithms adaptively scale all cards, sheets, and dialogs.
      </p>

      {/* Live Morphing Sample Card */}
      <div className="flex justify-center my-4">
        <div
          className="w-full max-w-xs h-28 bg-obsidian-950 border border-white/15 p-4 flex flex-col justify-between transition-all duration-150 shadow-lg"
          style={{ borderRadius: `${radius}px` }}
        >
          <div className="flex justify-between items-center">
            <span className="text-xs font-semibold text-zinc-300">Meditation Reps</span>
            <span
              className="w-2.5 h-2.5 bg-ruby-400"
              style={{ borderRadius: `${Math.max(2, radius * 0.4)}px` }}
            />
          </div>
          <div className="flex justify-between items-end">
            <span className="text-3xl font-extrabold num-mono text-white">108</span>
            <span
              className="text-[10px] px-2 py-1 bg-white/10 text-zinc-300 font-medium"
              style={{ borderRadius: `${Math.max(2, radius * 0.35)}px` }}
            >
              +1 step
            </span>
          </div>
        </div>
      </div>

      {/* Slider Control */}
      <div className="mt-6 pt-4 border-t border-white/5">
        <div className="flex justify-between text-[11px] text-zinc-500 mb-2 num-mono">
          <span>0dp (Sharp)</span>
          <span>16dp (Classic)</span>
          <span>32dp (Expressive)</span>
        </div>
        <input
          type="range"
          min="0"
          max="32"
          value={radius}
          onChange={handleChange}
          className="w-full accent-amber-400 bg-obsidian-950 h-2 rounded-lg cursor-pointer"
        />
      </div>
    </div>
  );
}

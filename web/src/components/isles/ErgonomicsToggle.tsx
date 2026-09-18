import React, { useState } from 'react';
import { playTactileTick, playHeavyPulse } from './SoundEffectsController';
import { Hand, Smartphone } from 'lucide-react';

export default function ErgonomicsToggle() {
  const [hand, setHand] = useState<'right' | 'left'>('right');

  const handleToggle = (selected: 'left' | 'right') => {
    if (selected !== hand) {
      playTactileTick(selected === 'left' ? 520 : 680);
      setHand(selected);
    }
  };

  return (
    <div className="w-full glass-panel rounded-3xl p-6 border border-white/10 bg-obsidian-900/80">
      <div className="flex items-center justify-between mb-4">
        <div className="flex items-center gap-2">
          <Hand className="w-4 h-4 text-amber-400" />
          <span className="text-xs uppercase tracking-wider text-zinc-400 font-semibold">Single-Hand Ergonomics</span>
        </div>
        <div className="flex items-center bg-obsidian-950 p-1 rounded-xl border border-white/5">
          <button
            onClick={() => handleToggle('left')}
            className={`px-2.5 py-1 rounded-lg text-xs font-medium transition-all ${
              hand === 'left' ? 'bg-amber-500 text-obsidian-950 font-bold' : 'text-zinc-400 hover:text-white'
            }`}
          >
            Left
          </button>
          <button
            onClick={() => handleToggle('right')}
            className={`px-2.5 py-1 rounded-lg text-xs font-medium transition-all ${
              hand === 'right' ? 'bg-amber-500 text-obsidian-950 font-bold' : 'text-zinc-400 hover:text-white'
            }`}
          >
            Right
          </button>
        </div>
      </div>

      <p className="text-xs text-zinc-400 mb-5 leading-relaxed">
        Primary touch targets dynamically align to the natural thumb arc for effortless single-handed operation.
      </p>

      {/* Simulated Phone Reach Arc */}
      <div className="relative w-full h-32 bg-obsidian-950 rounded-2xl border border-white/5 p-3 overflow-hidden flex flex-col justify-between">
        {/* Thumb Sweep Visual Arc */}
        <div
          className={`absolute bottom-0 w-36 h-36 rounded-full border border-dashed border-amber-400/40 bg-amber-500/5 transition-all duration-300 pointer-events-none ${
            hand === 'left' ? '-left-6 -bottom-6' : '-right-6 -bottom-6'
          }`}
        />

        <div className="flex justify-between items-center z-10 text-[11px] text-zinc-500">
          <span>Top App Bar</span>
          <Smartphone className="w-3.5 h-3.5" />
        </div>

        {/* Dynamic Action Button Placement */}
        <div className={`flex z-10 transition-all duration-300 ${hand === 'left' ? 'justify-start' : 'justify-end'}`}>
          <div className="px-4 py-2 rounded-xl bg-ruby-500 text-white font-bold text-xs shadow-md flex items-center gap-1.5 animate-pulse">
            <span>Primary Tap Target</span>
          </div>
        </div>
      </div>
    </div>
  );
}

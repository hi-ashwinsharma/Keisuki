import React, { useState } from 'react';
import { playTactileTick, playHeavyPulse } from './SoundEffectsController';
import { Vibrate } from 'lucide-react';

const HAPTIC_PROFILES = [
  { id: 'tick', label: 'Tick', desc: 'Subtle high-frequency snap', freq: 800 },
  { id: 'click', label: 'Click', desc: 'Standard tactile confirmation', freq: 600 },
  { id: 'heavy', label: 'Heavy', desc: 'Resonant threshold pulse', freq: 240, isHeavy: true },
];

export default function HapticsWaveVisualizer() {
  const [activeProfile, setActiveProfile] = useState(HAPTIC_PROFILES[1]);
  const [isVibrating, setIsVibrating] = useState(false);

  const handleTrigger = (profile: typeof HAPTIC_PROFILES[0]) => {
    setActiveProfile(profile);
    setIsVibrating(true);
    if (profile.isHeavy) {
      playHeavyPulse();
    } else {
      playTactileTick(profile.freq);
    }
    setTimeout(() => setIsVibrating(false), 200);
  };

  return (
    <div className="w-full glass-panel rounded-3xl p-6 border border-white/10 bg-obsidian-900">
      <div className="flex items-center justify-between mb-4">
        <div className="flex items-center gap-2">
          <Vibrate className="w-4 h-4 text-ruby-400" />
          <span className="text-xs uppercase tracking-wider text-zinc-400 font-semibold">Tactile Physics</span>
        </div>
        <span className="text-xs num-mono font-bold text-ruby-400">
          Precision Motors
        </span>
      </div>

      <p className="text-xs text-zinc-400 mb-5 leading-relaxed">
        Granular vibration profiles deliver physical mechanical confirmation with zero screen distraction.
      </p>

      {/* Waveform Visualization Bars */}
      <div className="h-20 bg-obsidian-950 rounded-2xl border border-white/5 p-4 flex items-center justify-center gap-1.5 overflow-hidden mb-5">
        {[20, 35, 60, 90, 100, 75, 45, 80, 95, 50, 30, 15].map((h, i) => (
          <div
            key={i}
            className={`w-1.5 rounded-full transition-all duration-150 ${
              isVibrating ? 'bg-ruby-500' : 'bg-white/10'
            }`}
            style={{
              height: isVibrating ? `${h}%` : '20%',
              transform: isVibrating ? `scaleY(${1 + Math.sin(i) * 0.3})` : 'scaleY(1)',
            }}
          />
        ))}
      </div>

      {/* Profile Buttons */}
      <div className="grid grid-cols-3 gap-2">
        {HAPTIC_PROFILES.map((p) => (
          <button
            key={p.id}
            onClick={() => handleTrigger(p)}
            className={`py-2 px-3 rounded-xl border text-xs font-semibold transition-all active:scale-95 ${
              activeProfile.id === p.id
                ? 'bg-ruby-500/20 border-ruby-500/40 text-ruby-300'
                : 'bg-white/5 border-white/5 text-zinc-400 hover:text-white'
            }`}
          >
            {p.label}
          </button>
        ))}
      </div>
    </div>
  );
}

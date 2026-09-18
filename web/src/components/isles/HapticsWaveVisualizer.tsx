import React, { useState } from 'react';
import { playTactileTick, playHeavyPulse } from './SoundEffectsController';
import { Vibrate, Wifi, Battery, Volume2, Sparkles, Plus } from 'lucide-react';

const HAPTIC_PROFILES = [
  { id: 'tick', label: 'Tick', desc: 'Subtle high-frequency snap', freq: 800 },
  { id: 'click', label: 'Click', desc: 'Standard tactile confirmation', freq: 580 },
  { id: 'heavy', label: 'Heavy', desc: 'Resonant threshold pulse', freq: 240, isHeavy: true },
];

export default function HapticsWaveVisualizer() {
  const [activeProfile, setActiveProfile] = useState(HAPTIC_PROFILES[1]);
  const [isVibrating, setIsVibrating] = useState(false);
  const [tapCount, setTapCount] = useState(64);

  const handleTrigger = (profile: typeof HAPTIC_PROFILES[0]) => {
    setActiveProfile(profile);
    setIsVibrating(true);
    setTapCount((c) => c + 1);
    if (profile.isHeavy) {
      playHeavyPulse();
    } else {
      playTactileTick(profile.freq);
    }
    setTimeout(() => setIsVibrating(false), 220);
  };

  return (
    <div className="w-full max-w-sm sm:max-w-md mx-auto">
      {/* Phone Outer Bezel Chassis */}
      <div className="relative rounded-[44px] p-3 bg-obsidian-950 border border-white/20 shadow-[0_30px_70px_-15px_rgba(0,0,0,0.95)]">
        
        {/* Device Screen Surface */}
        <div className="relative rounded-[36px] p-5 sm:p-6 bg-obsidian-900 border border-white/5 overflow-hidden">
          
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
              <Vibrate className="w-4 h-4 text-ruby-400" />
              <span className="text-xs font-bold tracking-wider text-white">TACTILE ENGINE</span>
            </div>
            <span className="text-xs num-mono font-bold text-ruby-400">
              Resonant Actuator
            </span>
          </div>

          {/* Interactive Tap Area with Tactile Feedback */}
          <div className="p-4 mb-3 rounded-2xl bg-obsidian-950 border border-white/5 text-center">
            <span className="text-[10px] uppercase text-zinc-500 font-bold block mb-1">
              Active Mantra / Laps
            </span>
            <div className="text-4xl font-extrabold num-mono text-white tracking-tight my-1">
              {tapCount}
            </div>
            <div className="text-[11px] text-zinc-400">
              Current profile: <span className="font-semibold text-amber-400">{activeProfile.label}</span>
            </div>
          </div>

          {/* Waveform Visualization Bars */}
          <div className="h-20 bg-obsidian-950 rounded-2xl border border-white/5 p-4 flex items-center justify-center gap-1.5 overflow-hidden mb-3">
            {[20, 35, 60, 90, 100, 85, 45, 80, 95, 55, 30, 15].map((h, i) => (
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

          {/* Profile Selector Buttons */}
          <div className="grid grid-cols-3 gap-2 mb-3">
            {HAPTIC_PROFILES.map((p) => (
              <button
                key={p.id}
                type="button"
                onClick={() => handleTrigger(p)}
                className={`py-2 px-2.5 rounded-xl border text-xs font-semibold transition-all active:scale-95 text-center ${
                  activeProfile.id === p.id
                    ? 'bg-ruby-500/20 border-ruby-500/40 text-ruby-300'
                    : 'bg-white/5 border-white/5 text-zinc-400 hover:text-white'
                }`}
              >
                <div>{p.label}</div>
                <div className="text-[9px] font-normal text-zinc-500 num-mono">{p.freq}Hz</div>
              </button>
            ))}
          </div>

          {/* Direct Tap Pad Trigger */}
          <button
            type="button"
            onClick={() => handleTrigger(activeProfile)}
            className="w-full py-3 rounded-2xl bg-white/10 hover:bg-white/15 text-white font-bold text-xs transition-all active:scale-95 border border-white/10 flex items-center justify-center gap-2 shadow-sm"
          >
            <Plus className="w-4 h-4 text-ruby-400" />
            <span>Feel Vibration Pulse (+1)</span>
          </button>

        </div>
      </div>
    </div>
  );
}

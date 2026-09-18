import React, { useState, useEffect } from 'react';
import { playTactileTick, playHeavyPulse, setMuted, getMuted } from './SoundEffectsController';
import { Volume2, VolumeX, RotateCcw, Plus, Minus, Activity, Check } from 'lucide-react';

const COLOR_TOKENS = [
  { id: 'ruby', name: 'Crimson Ruby', color: '#FF2E63' },
  { id: 'amber', name: 'Sunset Amber', color: '#FF9F1C' },
  { id: 'ivory', name: 'Warm Ivory', color: '#FFF8F0' },
  { id: 'emerald', name: 'Neon Jade', color: '#00E676' },
];

const STEP_OPTIONS = [1, 5, 10, 25];

export default function InteractiveCounterDemo() {
  const [count, setCount] = useState<number>(42);
  const [step, setStep] = useState<number>(1);
  const [selectedToken, setSelectedToken] = useState(COLOR_TOKENS[0]);
  const [isAudioMuted, setIsAudioMuted] = useState<boolean>(false);
  const [isPressing, setIsPressing] = useState<boolean>(false);
  const [tapsCount, setTapsCount] = useState<number>(18);
  const [lastAction, setLastAction] = useState<'+' | '-' | null>(null);

  useEffect(() => {
    setIsAudioMuted(getMuted());
  }, []);

  const handleIncrement = () => {
    playTactileTick(680);
    setCount((prev) => prev + step);
    setTapsCount((prev) => prev + 1);
    setLastAction('+');
    setIsPressing(true);
    setTimeout(() => setIsPressing(false), 120);
  };

  const handleDecrement = () => {
    if (count <= 0) return;
    playTactileTick(420);
    setCount((prev) => Math.max(0, prev - step));
    setTapsCount((prev) => prev + 1);
    setLastAction('-');
    setIsPressing(true);
    setTimeout(() => setIsPressing(false), 120);
  };

  const handleReset = () => {
    playHeavyPulse();
    setCount(0);
    setLastAction(null);
  };

  const toggleMute = () => {
    const nextMute = !isAudioMuted;
    setIsAudioMuted(nextMute);
    setMuted(nextMute);
    if (!nextMute) {
      playTactileTick(750);
    }
  };

  return (
    <div className="w-full max-w-md mx-auto relative group">
      {/* Simulated Device Frame */}
      <div className="relative rounded-[32px] p-6 sm:p-7 glass-panel border border-white/10 shadow-2xl backdrop-blur-xl bg-obsidian-900">
        
        {/* Device Top Bar */}
        <div className="flex items-center justify-between pb-5 border-b border-white/5">
          <div className="flex items-center space-x-2">
            <span className="w-2.5 h-2.5 rounded-full bg-emerald-500 animate-pulse" />
            <span className="text-xs uppercase tracking-widest text-zinc-400 font-medium">Live Simulator</span>
          </div>

          <div className="flex items-center space-x-2">
            <button
              onClick={toggleMute}
              title={isAudioMuted ? 'Unmute tactile clicks' : 'Mute tactile clicks'}
              className="p-2 rounded-xl bg-white/5 hover:bg-white/10 text-zinc-300 transition-colors border border-white/5 flex items-center gap-1.5 text-xs"
            >
              {isAudioMuted ? <VolumeX className="w-3.5 h-3.5 text-ruby-400" /> : <Volume2 className="w-3.5 h-3.5 text-amber-400" />}
              <span className="hidden sm:inline">{isAudioMuted ? 'Muted' : 'Audio On'}</span>
            </button>
            <button
              onClick={handleReset}
              title="Reset counter"
              className="p-2 rounded-xl bg-white/5 hover:bg-white/10 text-zinc-400 hover:text-white transition-colors border border-white/5"
            >
              <RotateCcw className="w-3.5 h-3.5" />
            </button>
          </div>
        </div>

        {/* Counter Display Surface */}
        <div className="py-8 text-center flex flex-col items-center justify-center">
          <div className="flex items-center gap-2 mb-2">
            <span className="text-xs font-semibold uppercase tracking-wider text-zinc-400">
              Daily Rituals
            </span>
          </div>

          {/* Big Rolling Count Digit */}
          <div className="relative my-2 select-none">
            <div
              className={`text-7xl sm:text-8xl font-extrabold num-mono tracking-tight transition-transform duration-100 ${
                isPressing ? (lastAction === '+' ? 'scale-105' : 'scale-95') : 'scale-100'
              }`}
              style={{ color: selectedToken.color }}
            >
              {count.toLocaleString()}
            </div>
          </div>

          {/* Velocity & Telemetry Stats */}
          <div className="flex items-center gap-4 mt-3 text-xs text-zinc-400 num-mono">
            <span className="flex items-center gap-1">
              <Activity className="w-3.5 h-3.5 text-amber-400" />
              {tapsCount} total taps
            </span>
            <span>•</span>
            <span className="text-zinc-500">Step: +{step}</span>
          </div>
        </div>

        {/* Main Action Pad */}
        <div className="grid grid-cols-4 gap-3 mb-5">
          <button
            onClick={handleDecrement}
            disabled={count === 0}
            className={`col-span-1 h-20 rounded-2xl flex items-center justify-center border transition-all active:scale-95 ${
              count === 0
                ? 'opacity-30 cursor-not-allowed border-white/5 bg-white/[0.02]'
                : 'bg-white/5 hover:bg-white/10 border-white/10 text-zinc-200 hover:border-white/20'
            }`}
          >
            <Minus className="w-6 h-6" />
          </button>

          <button
            onClick={handleIncrement}
            className={`col-span-3 h-20 rounded-2xl font-bold text-xl flex items-center justify-center gap-2 transition-all duration-75 shadow-lg active:scale-95 select-none ${
              selectedToken.id === 'ivory' ? 'text-obsidian-950' : 'text-white'
            }`}
            style={{
              backgroundColor: selectedToken.color,
            }}
          >
            <Plus className="w-6 h-6 stroke-[3]" />
            <span>Tap to Increment</span>
          </button>
        </div>

        {/* Step Selector */}
        <div className="flex items-center justify-between pt-4 border-t border-white/5">
          <span className="text-xs text-zinc-500 font-medium">Step Size</span>
          <div className="flex items-center gap-1.5 bg-obsidian-950 p-1 rounded-xl border border-white/5">
            {STEP_OPTIONS.map((s) => (
              <button
                key={s}
                onClick={() => {
                  playTactileTick(500);
                  setStep(s);
                }}
                className={`px-3 py-1 rounded-lg text-xs num-mono font-medium transition-all ${
                  step === s ? 'bg-white/15 text-white shadow-sm' : 'text-zinc-400 hover:text-white'
                }`}
              >
                +{s}
              </button>
            ))}
          </div>
        </div>

        {/* Theme Color Picker */}
        <div className="flex items-center justify-between pt-3 mt-3 border-t border-white/5">
          <span className="text-xs text-zinc-500 font-medium">Color Token</span>
          <div className="flex items-center gap-2">
            {COLOR_TOKENS.map((token) => (
              <button
                key={token.id}
                onClick={() => {
                  playTactileTick(550);
                  setSelectedToken(token);
                }}
                title={token.name}
                className={`w-6 h-6 rounded-full transition-transform flex items-center justify-center ${
                  selectedToken.id === token.id ? 'scale-125 ring-2 ring-white/60' : 'hover:scale-110 opacity-70'
                }`}
                style={{ backgroundColor: token.color }}
              >
                {selectedToken.id === token.id && (
                  <Check className={`w-3 h-3 ${token.id === 'ivory' ? 'text-black' : 'text-white'}`} />
                )}
              </button>
            ))}
          </div>
        </div>

      </div>
    </div>
  );
}

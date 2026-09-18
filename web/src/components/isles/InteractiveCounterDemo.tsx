import React, { useState, useEffect, useRef } from 'react';
import { playTactileTick, playHeavyPulse, setMuted, getMuted } from './SoundEffectsController';
import { Volume2, VolumeX, RotateCcw, Plus, Minus, Activity, Check, Wifi, Battery } from 'lucide-react';

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

  // Scroll & mouse driven 3D tilt
  const containerRef = useRef<HTMLDivElement | null>(null);
  const [tilt, setTilt] = useState<{ x: number; y: number }>({ x: 3, y: -6 });

  useEffect(() => {
    setIsAudioMuted(getMuted());

    const handleScroll = () => {
      if (!containerRef.current) return;
      const rect = containerRef.current.getBoundingClientRect();
      const windowHeight = window.innerHeight;
      // Calculate normalized progress of this element through the viewport (-1 to 1)
      const centerOffset = (rect.top + rect.height / 2 - windowHeight / 2) / (windowHeight / 2);
      const targetRotateX = Math.max(-10, Math.min(10, centerOffset * 6));
      const targetRotateY = Math.max(-12, Math.min(6, -6 - centerOffset * 4));
      setTilt({ x: targetRotateX, y: targetRotateY });
    };

    window.addEventListener('scroll', handleScroll, { passive: true });
    handleScroll();

    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  const handleMouseMove = (e: React.MouseEvent<HTMLDivElement>) => {
    const rect = e.currentTarget.getBoundingClientRect();
    const x = (e.clientX - rect.left) / rect.width - 0.5;
    const y = (e.clientY - rect.top) / rect.height - 0.5;
    setTilt({
      x: -(y * 12),
      y: x * 14,
    });
  };

  const handleMouseLeave = () => {
    setTilt({ x: 3, y: -6 });
  };

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
    <div
      ref={containerRef}
      onMouseMove={handleMouseMove}
      onMouseLeave={handleMouseLeave}
      className="w-full max-w-sm sm:max-w-md mx-auto transition-transform duration-300 ease-out"
      style={{
        transform: `perspective(1200px) rotateX(${tilt.x}deg) rotateY(${tilt.y}deg)`,
        transformStyle: 'preserve-3d',
      }}
    >
      {/* Architectural Wireframe Outer Chassis */}
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

          {/* App Top Bar */}
          <div className="flex items-center justify-between py-4 border-b border-white/5">
            <div className="flex items-center space-x-2">
              <span className="w-2 h-2 rounded-full bg-emerald-500 animate-pulse" />
              <span className="text-xs font-bold tracking-wider text-white">KEISUKI FOCUS</span>
            </div>

            <div className="flex items-center space-x-1.5">
              <button
                onClick={toggleMute}
                title={isAudioMuted ? 'Unmute' : 'Mute'}
                className="p-1.5 rounded-lg bg-white/5 hover:bg-white/10 text-zinc-300 border border-white/5 text-xs"
              >
                {isAudioMuted ? <VolumeX className="w-3.5 h-3.5 text-ruby-400" /> : <Volume2 className="w-3.5 h-3.5 text-amber-400" />}
              </button>
              <button
                onClick={handleReset}
                title="Reset counter"
                className="p-1.5 rounded-lg bg-white/5 hover:bg-white/10 text-zinc-400 hover:text-white border border-white/5"
              >
                <RotateCcw className="w-3.5 h-3.5" />
              </button>
            </div>
          </div>

          {/* Counter Display Surface */}
          <div className="py-7 text-center flex flex-col items-center justify-center">
            <span className="text-[11px] font-semibold uppercase tracking-wider text-zinc-400 mb-1">
              Daily Rituals
            </span>

            {/* Big Rolling Count Digit */}
            <div className="relative my-2 select-none">
              <div
                className={`text-6xl sm:text-7xl font-extrabold num-mono tracking-tight transition-transform duration-100 ${
                  isPressing ? (lastAction === '+' ? 'scale-105' : 'scale-95') : 'scale-100'
                }`}
                style={{ color: selectedToken.color }}
              >
                {count.toLocaleString()}
              </div>
            </div>

            {/* Velocity & Telemetry Badges */}
            <div className="flex items-center gap-3 mt-2 text-xs text-zinc-400 num-mono">
              <span className="flex items-center gap-1">
                <Activity className="w-3.5 h-3.5 text-amber-400" />
                {tapsCount} taps
              </span>
              <span>•</span>
              <span className="text-zinc-500">Step: +{step}</span>
            </div>
          </div>

          {/* Main Action Pad */}
          <div className="grid grid-cols-4 gap-2.5 mb-4">
            <button
              onClick={handleDecrement}
              disabled={count === 0}
              className={`col-span-1 h-16 rounded-2xl flex items-center justify-center border transition-all active:scale-95 ${
                count === 0
                  ? 'opacity-30 cursor-not-allowed border-white/5 bg-white/[0.02]'
                  : 'bg-white/5 hover:bg-white/10 border-white/10 text-zinc-200'
              }`}
            >
              <Minus className="w-5 h-5" />
            </button>

            <button
              onClick={handleIncrement}
              className={`col-span-3 h-16 rounded-2xl font-bold text-lg flex items-center justify-center gap-2 transition-all duration-75 shadow-lg active:scale-95 select-none ${
                selectedToken.id === 'ivory' ? 'text-obsidian-950' : 'text-white'
              }`}
              style={{
                backgroundColor: selectedToken.color,
              }}
            >
              <Plus className="w-5 h-5 stroke-[3]" />
              <span>Tap to Count</span>
            </button>
          </div>

          {/* Step Selector */}
          <div className="flex items-center justify-between pt-3 border-t border-white/5">
            <span className="text-[11px] text-zinc-500 font-medium">Step</span>
            <div className="flex items-center gap-1 bg-obsidian-950 p-1 rounded-xl border border-white/5">
              {STEP_OPTIONS.map((s) => (
                <button
                  key={s}
                  onClick={() => {
                    playTactileTick(500);
                    setStep(s);
                  }}
                  className={`px-2.5 py-0.5 rounded-lg text-xs num-mono font-medium transition-all ${
                    step === s ? 'bg-white/15 text-white' : 'text-zinc-400 hover:text-white'
                  }`}
                >
                  +{s}
                </button>
              ))}
            </div>
          </div>

          {/* Color Token Selector */}
          <div className="flex items-center justify-between pt-2.5 mt-2.5 border-t border-white/5">
            <span className="text-[11px] text-zinc-500 font-medium">Palette</span>
            <div className="flex items-center gap-2">
              {COLOR_TOKENS.map((token) => (
                <button
                  key={token.id}
                  onClick={() => {
                    playTactileTick(550);
                    setSelectedToken(token);
                  }}
                  title={token.name}
                  className={`w-5 h-5 rounded-full transition-transform flex items-center justify-center ${
                    selectedToken.id === token.id ? 'scale-125 ring-2 ring-white/60' : 'hover:scale-110 opacity-70'
                  }`}
                  style={{ backgroundColor: token.color }}
                >
                  {selectedToken.id === token.id && (
                    <Check className={`w-2.5 h-2.5 ${token.id === 'ivory' ? 'text-black' : 'text-white'}`} />
                  )}
                </button>
              ))}
            </div>
          </div>

          {/* Bottom Wireframe Home Bar */}
          <div className="w-24 h-1 bg-white/20 rounded-full mx-auto mt-4" />

        </div>
      </div>
    </div>
  );
}

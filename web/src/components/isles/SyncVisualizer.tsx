import React, { useState, useEffect } from 'react';
import { playTactileTick, playHeavyPulse } from './SoundEffectsController';
import { Database, Cloud, Zap, ArrowRight, Clock, Wifi, Battery, Hand } from 'lucide-react';

export default function SyncVisualizer() {
  const [localWrites, setLocalWrites] = useState<number>(0);
  const [cloudSynced, setCloudSynced] = useState<number>(0);
  const [isDebouncing, setIsDebouncing] = useState<boolean>(false);
  const [progress, setProgress] = useState<number>(0);
  const [hand, setHand] = useState<'right' | 'left'>('right');

  const handleSimulatedTap = () => {
    playTactileTick(700);
    setLocalWrites((prev) => prev + 1);
    setIsDebouncing(true);
    setProgress(0);
  };

  const handleHandToggle = (selected: 'left' | 'right') => {
    if (selected !== hand) {
      playTactileTick(selected === 'left' ? 520 : 680);
      setHand(selected);
    }
  };

  useEffect(() => {
    if (!isDebouncing) return;

    const interval = setInterval(() => {
      setProgress((prev) => {
        if (prev >= 100) {
          clearInterval(interval);
          setIsDebouncing(false);
          setCloudSynced(localWrites);
          playHeavyPulse();
          return 100;
        }
        return prev + 10;
      });
    }, 150);

    return () => clearInterval(interval);
  }, [isDebouncing, localWrites]);

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
              <Zap className="w-4 h-4 text-ruby-400" />
              <span className="text-xs font-bold tracking-wider text-white">SYNC & FLOW ENGINE</span>
            </div>
            <span className="text-xs num-mono font-bold text-emerald-400">
              0ms Local Latency
            </span>
          </div>

          {/* Sync Flow Pipeline Card */}
          <div className="grid grid-cols-5 items-center gap-2 my-2 bg-obsidian-950 p-3.5 rounded-2xl border border-white/5">
            {/* Local Storage */}
            <div className="col-span-2 p-2.5 rounded-xl bg-white/5 border border-white/10 text-center">
              <Database className="w-4 h-4 mx-auto text-amber-400 mb-1" />
              <span className="text-[11px] font-bold text-white block">Room DB</span>
              <span className="text-[9px] text-emerald-400 num-mono font-medium">0ms Instant</span>
              <div className="mt-0.5 text-xs num-mono font-bold text-amber-300">
                {localWrites} writes
              </div>
            </div>

            {/* Transition Arrow */}
            <div className="col-span-1 flex flex-col items-center justify-center">
              <div className={`transition-all duration-300 ${isDebouncing ? 'text-ruby-400 animate-pulse' : 'text-zinc-600'}`}>
                <ArrowRight className="w-4 h-4 mx-auto" />
              </div>
              <span className="text-[8px] num-mono text-zinc-500 mt-0.5">1500ms</span>
            </div>

            {/* Cloud Firestore */}
            <div className="col-span-2 p-2.5 rounded-xl bg-white/5 border border-white/10 text-center">
              <Cloud className="w-4 h-4 mx-auto text-ruby-400 mb-1" />
              <span className="text-[11px] font-bold text-white block">Firestore</span>
              <span className="text-[9px] text-zinc-400 num-mono font-medium">Debounced</span>
              <div className="mt-0.5 text-xs num-mono font-bold text-ruby-300">
                {cloudSynced} synced
              </div>
            </div>
          </div>

          {/* Debounce Buffer Status */}
          <div className="mb-3 px-1">
            <div className="flex justify-between text-[10px] text-zinc-400 mb-1">
              <span className="flex items-center gap-1">
                <Clock className="w-3 h-3 text-amber-400" />
                {isDebouncing ? 'Debounce buffer active...' : 'Cloud in sync'}
              </span>
              <span className="num-mono">{isDebouncing ? `${Math.round(1500 * (1 - progress / 100))}ms` : 'Idle'}</span>
            </div>
            <div className="w-full bg-obsidian-950 h-1.5 rounded-full overflow-hidden border border-white/5">
              <div
                className="h-full bg-amber-500 transition-all duration-150"
                style={{ width: `${progress}%` }}
              />
            </div>
          </div>

          {/* Single-Hand Ergonomics Arc Selector */}
          <div className="p-3 mb-3 bg-obsidian-950 rounded-2xl border border-white/5 relative overflow-hidden">
            <div
              className={`absolute bottom-0 w-32 h-32 rounded-full border border-dashed border-amber-400/40 bg-amber-500/5 transition-all duration-300 pointer-events-none ${
                hand === 'left' ? '-left-6 -bottom-6' : '-right-6 -bottom-6'
              }`}
            />
            
            <div className="flex items-center justify-between mb-2 relative z-10">
              <div className="flex items-center gap-1.5">
                <Hand className="w-3.5 h-3.5 text-amber-400" />
                <span className="text-[11px] font-semibold text-zinc-300">Thumb Reach Arc</span>
              </div>

              <div className="flex items-center bg-obsidian-900 p-0.5 rounded-lg border border-white/10">
                <button
                  type="button"
                  onClick={() => handleHandToggle('left')}
                  className={`px-2 py-0.5 rounded text-[10px] font-medium transition-all ${
                    hand === 'left' ? 'bg-amber-500 text-obsidian-950 font-bold' : 'text-zinc-400 hover:text-white'
                  }`}
                >
                  Left
                </button>
                <button
                  type="button"
                  onClick={() => handleHandToggle('right')}
                  className={`px-2 py-0.5 rounded text-[10px] font-medium transition-all ${
                    hand === 'right' ? 'bg-amber-500 text-obsidian-950 font-bold' : 'text-zinc-400 hover:text-white'
                  }`}
                >
                  Right
                </button>
              </div>
            </div>

            <div className={`flex relative z-10 transition-all duration-300 ${hand === 'left' ? 'justify-start' : 'justify-end'}`}>
              <span className="text-[9px] num-mono text-amber-400/80 px-2 py-0.5 bg-white/5 rounded">
                Optimal touch zone aligned
              </span>
            </div>
          </div>

          {/* Trigger Mutation Tap Button */}
          <button
            type="button"
            onClick={handleSimulatedTap}
            className="w-full py-2.5 rounded-2xl bg-ruby-500 hover:bg-ruby-600 text-white font-bold text-xs transition-all active:scale-95 shadow-md flex items-center justify-center gap-2"
          >
            <Zap className="w-3.5 h-3.5" />
            <span>Trigger Mutation Event (+1)</span>
          </button>

        </div>
      </div>
    </div>
  );
}

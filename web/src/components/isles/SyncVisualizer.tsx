import React, { useState, useEffect } from 'react';
import { playTactileTick, playHeavyPulse } from './SoundEffectsController';
import { Database, Cloud, Zap, ArrowRight, Clock } from 'lucide-react';

export default function SyncVisualizer() {
  const [localWrites, setLocalWrites] = useState<number>(0);
  const [cloudSynced, setCloudSynced] = useState<number>(0);
  const [isDebouncing, setIsDebouncing] = useState<boolean>(false);
  const [progress, setProgress] = useState<number>(0);

  const handleSimulatedTap = () => {
    playTactileTick(700);
    setLocalWrites((prev) => prev + 1);
    setIsDebouncing(true);
    setProgress(0);
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
    <div className="w-full glass-panel rounded-3xl p-6 border border-white/10 bg-obsidian-900">
      <div className="flex items-center justify-between mb-4">
        <div className="flex items-center gap-2">
          <Zap className="w-4 h-4 text-ruby-400" />
          <span className="text-xs uppercase tracking-wider text-zinc-400 font-semibold">Offline-First Engine</span>
        </div>
        <span className="text-xs num-mono font-bold text-emerald-400">
          Zero-Lag
        </span>
      </div>

      <p className="text-xs text-zinc-400 mb-5 leading-relaxed">
        Mutations write immediately to local Room SQLite, then debounce 1500ms before cloud syncing.
      </p>

      {/* Sync Flow Diagram */}
      <div className="grid grid-cols-5 items-center gap-2 my-4 bg-obsidian-950 p-4 rounded-2xl border border-white/5">
        {/* Local Storage */}
        <div className="col-span-2 p-3 rounded-xl bg-white/5 border border-white/10 text-center">
          <Database className="w-5 h-5 mx-auto text-amber-400 mb-1" />
          <span className="text-[11px] font-bold text-white block">Room DB</span>
          <span className="text-[10px] text-emerald-400 num-mono font-medium">0ms Instant</span>
          <div className="mt-1 text-xs num-mono font-bold text-amber-300">
            {localWrites} writes
          </div>
        </div>

        {/* Transition Arrow */}
        <div className="col-span-1 flex flex-col items-center justify-center">
          <div className={`transition-all duration-300 ${isDebouncing ? 'text-ruby-400 animate-pulse' : 'text-zinc-600'}`}>
            <ArrowRight className="w-5 h-5 mx-auto" />
          </div>
          <span className="text-[9px] num-mono text-zinc-500 mt-1">1500ms</span>
        </div>

        {/* Cloud Firestore */}
        <div className="col-span-2 p-3 rounded-xl bg-white/5 border border-white/10 text-center">
          <Cloud className="w-5 h-5 mx-auto text-ruby-400 mb-1" />
          <span className="text-[11px] font-bold text-white block">Firestore</span>
          <span className="text-[10px] text-zinc-400 num-mono font-medium">Debounced</span>
          <div className="mt-1 text-xs num-mono font-bold text-ruby-300">
            {cloudSynced} synced
          </div>
        </div>
      </div>

      {/* Debounce Progress Bar */}
      <div className="mb-4">
        <div className="flex justify-between text-[11px] text-zinc-400 mb-1.5">
          <span className="flex items-center gap-1">
            <Clock className="w-3 h-3 text-amber-400" />
            {isDebouncing ? 'Debounce buffer active...' : 'Cloud in sync'}
          </span>
          <span className="num-mono">{isDebouncing ? `${Math.round(1500 * (1 - progress / 100))}ms` : 'Idle'}</span>
        </div>
        <div className="w-full bg-obsidian-950 h-2 rounded-full overflow-hidden border border-white/5">
          <div
            className="h-full bg-amber-500 transition-all duration-150"
            style={{ width: `${progress}%` }}
          />
        </div>
      </div>

      {/* Action Button */}
      <button
        onClick={handleSimulatedTap}
        className="w-full py-2.5 rounded-xl bg-white/10 hover:bg-white/15 text-white font-semibold text-xs transition-all active:scale-98 border border-white/10 flex items-center justify-center gap-2"
      >
        <Zap className="w-3.5 h-3.5 text-amber-400" />
        <span>Trigger Mutation Event</span>
      </button>
    </div>
  );
}

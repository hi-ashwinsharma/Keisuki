import React, { useState, useEffect } from 'react';
import { Github, Download } from 'lucide-react';

export default function FloatingNavbar() {
  const [isScrolled, setIsScrolled] = useState(false);

  useEffect(() => {
    const handleScroll = () => {
      setIsScrolled(window.scrollY > 40);
    };

    window.addEventListener('scroll', handleScroll, { passive: true });
    handleScroll();

    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  return (
    <div className="fixed top-0 left-0 right-0 z-50 flex justify-center px-3 pt-3 sm:pt-4 pointer-events-none">
      <header
        className={`pointer-events-auto flex items-center justify-between transition-all duration-300 ${
          isScrolled
            ? 'w-full max-w-4xl py-2 px-4 sm:px-5 rounded-full bg-obsidian-900/95 backdrop-blur-xl border border-white/10 shadow-[0_12px_40px_-10px_rgba(0,0,0,0.8)]'
            : 'w-full max-w-6xl px-4 sm:px-6 py-3 rounded-none bg-transparent border border-transparent'
        }`}
      >
        {/* Brand Logo */}
        <a href="/" className="flex items-center gap-2.5 group">
          <div className="w-7 h-7 rounded-xl bg-gradient-to-tr from-ruby-500 to-amber-400 p-[1px] shadow-md group-hover:scale-105 transition-transform flex-shrink-0">
            <div className="w-full h-full bg-obsidian-950 rounded-[11px] p-1 flex items-center justify-center">
              <svg className="w-full h-full text-white" viewBox="50 44 412 424" fill="currentColor">
                <rect x="136" y="130" width="26" height="252" rx="4" />
                <rect x="180" y="130" width="26" height="252" rx="4" />
                <rect x="224" y="130" width="26" height="252" rx="4" />
                <path d="M 250,230 L 350,130 H 376 V 156 L 276,256 L 376,356 V 382 H 350 L 250,282 Z" />
              </svg>
            </div>
          </div>
          <span className="text-sm font-bold tracking-tight text-white group-hover:text-amber-300 transition-colors">
            Keisuki
          </span>
        </a>

        {/* Grouped Navigation Links */}
        <nav className="hidden md:flex items-center text-xs font-medium text-zinc-400">
          {/* Group 1: In-Page Anchors */}
          <div className="flex items-center gap-5">
            <a href="/#showcase" className="hover:text-white transition-colors">Showcase</a>
            <a href="/#features" className="hover:text-white transition-colors">Features</a>
            <a href="/#telemetry" className="hover:text-white transition-colors">Telemetry</a>
          </div>

          {/* Group Divider */}
          <div className="w-px h-3.5 bg-white/10 mx-4" />

          {/* Group 2: Standalone Pages (No Icons) */}
          <div className="flex items-center gap-5">
            <a href="/docs" className="hover:text-amber-400 transition-colors">Docs</a>
            <a href="/changelog" className="hover:text-amber-400 transition-colors">Changelog</a>
            <a href="/privacy" className="hover:text-amber-400 transition-colors">Privacy</a>
          </div>
        </nav>

        {/* Header Actions */}
        <div className="flex items-center gap-2 sm:gap-3">
          <a
            href="https://github.com/hi-ashwinsharma/Keisuki"
            target="_blank"
            rel="noopener noreferrer"
            className="flex items-center gap-1.5 px-3 py-1.5 rounded-full bg-white/5 hover:bg-white/10 text-white text-xs font-medium border border-white/10 transition-all hover:border-white/20"
          >
            <Github className="w-3.5 h-3.5 text-zinc-300" />
            <span className="hidden sm:inline">GitHub</span>
            <span className="text-[10px] num-mono text-zinc-400">v1.0</span>
          </a>

          <a
            href="/#download"
            className="flex items-center gap-1.5 px-3.5 py-1.5 rounded-full bg-ruby-500 hover:bg-ruby-600 text-white text-xs font-bold transition-all active:scale-95"
          >
            <Download className="w-3.5 h-3.5" />
            <span>Get App</span>
          </a>
        </div>
      </header>
    </div>
  );
}

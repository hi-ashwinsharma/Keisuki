/** @type {import('tailwindcss').Config} */
export default {
  content: ['./src/**/*.{astro,html,js,jsx,md,mdx,svelte,ts,tsx,vue}'],
  theme: {
    extend: {
      colors: {
        obsidian: {
          950: '#060608',
          900: '#0A0A0C',
          850: '#0F0F14',
          800: '#14141A',
          750: '#1A1A22',
          700: '#22222E',
        },
        ruby: {
          400: '#FF5E85',
          500: '#FF2E63',
          600: '#E01A4F',
          700: '#B80D3B',
          glow: 'rgba(255, 46, 99, 0.25)',
        },
        amber: {
          400: '#FFB703',
          500: '#FF9F1C',
          600: '#FB8500',
          glow: 'rgba(255, 159, 28, 0.25)',
        },
        ivory: {
          50: '#FFFFFF',
          100: '#FFFDF9',
          200: '#FFF8F0',
          300: '#EBE4DC',
          400: '#C7BFB5',
          500: '#9E978E',
        },
      },
      fontFamily: {
        sans: ['"Plus Jakarta Sans"', 'system-ui', '-apple-system', 'sans-serif'],
        serif: ['"Instrument Serif"', 'Georgia', 'serif'],
        mono: ['"JetBrains Mono"', 'ui-monospace', 'monospace'],
      },
      boxShadow: {
        'ruby-glow': '0 0 35px -5px rgba(255, 46, 99, 0.3)',
        'amber-glow': '0 0 35px -5px rgba(255, 159, 28, 0.3)',
        'card-glow': '0 8px 32px 0 rgba(0, 0, 0, 0.37)',
      },
    },
  },
  plugins: [],
};

const { performance } = require('perf_hooks');

const state = {
    recentTracks: [],
    favorites: []
};

// Generate 2000 recent tracks
for (let i = 0; i < 2000; i++) {
    state.recentTracks.push({ artist: 'Artist' + i, title: 'Title' + i, time: 'Now' });
}
// Generate 1000 favorites
for (let i = 0; i < 1000; i++) {
    state.favorites.push({ key: 'Artist' + (i*2) + ' - Title' + (i*2), artist: 'Artist' + (i*2), title: 'Title' + (i*2) });
}

function escapeHtml(str) {
    return String(str || '')
      .replace(/&/g, '&amp;')
      .replace(/</g, '&lt;')
      .replace(/>/g, '&gt;')
      .replace(/"/g, '&quot;');
}

function renderRecentTracks_old() {
    return state.recentTracks.map(item => {
      const key = `${item.artist} - ${item.title}`;
      const isFav = state.favorites.some(f => f.key === key);
      return `<div class="${isFav ? 'fav' : ''}">${escapeHtml(item.title)}</div>`;
    }).join('');
}

function renderRecentTracks_new() {
    const favKeys = new Set(state.favorites.map(f => f.key));
    return state.recentTracks.map(item => {
      const key = `${item.artist} - ${item.title}`;
      const isFav = favKeys.has(key);
      return `<div class="${isFav ? 'fav' : ''}">${escapeHtml(item.title)}</div>`;
    }).join('');
}

const RUNS = 1000;

console.log("Measuring Baseline...");
let start = performance.now();
for (let i = 0; i < RUNS; i++) renderRecentTracks_old();
const oldTime = performance.now() - start;
console.log(`Baseline: ${oldTime.toFixed(2)} ms for ${RUNS} runs`);

console.log("Measuring Optimized...");
start = performance.now();
for (let i = 0; i < RUNS; i++) renderRecentTracks_new();
const newTime = performance.now() - start;
console.log(`Optimized: ${newTime.toFixed(2)} ms for ${RUNS} runs`);

console.log(`Improvement: ${(oldTime / newTime).toFixed(2)}x faster`);

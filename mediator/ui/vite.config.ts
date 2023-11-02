import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

import mix from 'vite-plugin-mix'

export default defineConfig({
  plugins: [
    react(),
    mix.default({
      handler: './server.js',
    })
  ],
  server: {
    proxy: {
      '/api/sessions': {
        target: 'http://0.0.0.0:8080',
        changeOrigin: true,
        rewrite: (url) => {
          console.log(url.replace('/api', ''))
          return url.replace('/api', '');
        }
      }
    }
  }
})

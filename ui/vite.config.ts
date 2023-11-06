import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [
    react(),
  ],
  server: {
    proxy: {
      '/api/sessions': {
        target: 'http://0.0.0.0:8080',
        changeOrigin: false,
        rewrite: (url) => {
          return url.replace('/api', '');
        }
      }
    }
  }
})

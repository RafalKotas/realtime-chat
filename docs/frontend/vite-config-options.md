# Vite configuration examples

This document presents common configuration options that can be used in `vite.config.ts`.

These examples demonstrate typical capabilities of Vite configuration used in real-world projects.

--- 

## Example configuration

```typescript
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import path from 'path'

export default defineConfig({
  plugins: [react()],

  server: {
    port: 3000,
    open: true,
    strictPort: true
  },

  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src')
    }
  },

  build: {
    outDir: 'dist',
    sourcemap: true,
    minify: 'esbuild'
  },

  define: {
    __APP_VERSION__: '"1.0.0"'
  }
})
```
---

## plugins

Registers Vite plugins that extend the functionality of the development server and build process.

Example

```typescript
plugins: [react()]
```

Plugins can provide features such as:
- framework support (React, Vue, Svelte)
- code linting
- image optimization
- environment variable handling

--- 

## server

Configures the development server used during local development.

Example:

```typescript
server: {
  port: 3000,
  open: true,
  strictPort: true
}
```

Explanation:

| option     | description                                  |
| ---------- | -------------------------------------------- |
| port       | specifies the port used by the dev server    |
| open       | automatically opens the browser              |
| strictPort | prevents Vite from switching to another port |


--- 

## resolve.alias

Allows defining custom import paths.

Example:

```typescript
resolve: {
  alias: {
    '@': path.resolve(__dirname, './src')
  }
}
```

Usage:

```typescript
import Button from '@/components/Button'
```

instead of:

```typescript
import Button from '../../../components/Button'
```

---

## build

Configures the production build process.

Example:

```typescript
build: {
  outDir: 'dist',
  sourcemap: true,
  minify: 'esbuild'
}
```

Explanation:

| option    | description                                   |
| --------- | --------------------------------------------- |
| outDir    | output directory for the production build     |
| sourcemap | generates source maps                         |
| minify    | defines which tool performs code minification |

---

## define

Allows defining global constants replaced at build time.

Example:

```typescript
define: {
  __APP_VERSION__: '"1.0.0"'
}
```

Usage in code:
console.log(__APP_VERSION__)

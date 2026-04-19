# `eslint.config.js`

## Purpose

This file defines the configuration for ESLint, a tool used to analyze code and enforce consistent coding standards.

It specifies which rules should be applied to the codebase, what language features are supported, and which environments or plugins are used.

## What it does

- Enforces code quality and consistency
- Detects potential errors and enforces best practices
- Applies recommended rule sets for JavaScript and TypeScript
- Integrates plugins (e.g. React Hooks, React Refresh)
- Defines which files should be linted and which should be ignored

## Why it matters

Using ESLint helps maintain a clean, readable, and predictable codebase, especially in team environments. It also prevents common bugs and enforces best practices automatically.

## Code explanation

### `import(s)`

```javascript
// Core ESLint JavaScript rules (includes recommended config)
import js from '@eslint/js'

// Predefined global variables for different environments (e.g. browser, node)
import globals from 'globals'

// ESLint plugin enforcing React Hooks rules and best practices
import reactHooks from 'eslint-plugin-react-hooks' 

// ESLint plugin for React Fast Refresh (used in Vite and modern React setups)
import reactRefresh from 'eslint-plugin-react-refresh' 

// TypeScript support for ESLint (parser, rules, and recommended configs)
import tseslint from 'typescript-eslint'

// Helpers for defining flat ESLint config and global ignore patterns
import { defineConfig, globalIgnores } from 'eslint/config' 
```

### `defineConfig`

```javascript
// Exports the ESLint configuration using the flat config format
// defineConfig helps with type safety, validation, and better IDE support
export default defineConfig([...])
```

1. `globalIgnores(['dist'])` - globally ignores the "dist" directory (build output) from linting
2. `files: ['**/*.{ts,tsx}']` - applies this configuration only to TypeScript files (.ts and .tsx)
(`**/*` - matches files in all directories recursively)
3. `extends:` - extends recommended rule sets for JavaScript, TypeScript, React Hooks, and React Refresh
(use predefined rule sets instead of defining rules manually)
- `js.configs.recommended` - basic ESLint rules for JavaScript (e.g. no undefined variables, no unused variables)
- `tseslint.configs.recommended` - rules for TypeScript, TS parser integration, additional type checks
- `reactHooks.configs.flat.recommended` - rules for React Hooks, e.g. proper use of `useEffect`, hooks dependencies
- `reactRefresh.configs.vite` - rules for React Fast Refresh (Vite), ensures proper hot reload behavior

In general, using predefined rule sets is preferred over defining rules manually.

4. `languageOptions` - defines how ESLint interprets the code

```javascript
// Defines language settings such as ECMAScript version and global variables
languageOptions: {

  // Specifies the ECMAScript version (syntax/features) used in the project
  ecmaVersion: 2020,

  // Provides predefined browser global variables (e.g. window, document)
  globals: globals.browser,
},
```

- `ecmaVersion: 2020` - enables modern JavaScript syntax (e.g. optional chaining, nullish coalescing, dynamic import)
- `globals: globals.browser` - adds global variables available in browser, e.g.:
    - `window`
    - `document`
    - `console`
    - `localStorage`

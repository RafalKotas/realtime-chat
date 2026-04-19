# vite.config.ts file

## Overview

`vite.config.ts` is the main configuration file for **Vite**, the build tool used to run and bundle the application.

This file allows developers to customize how Vite behaves during development and production builds.

Typical responsibilities of this file include:

- registering **Vite plugins**
- configuring the **development server**
- defining **build options**
- customizing how modules are processed

In this project, the configuration registers the **React plugin**, which enables React-specific features such as JSX/TSX support and React Fast Refresh.

## Initial content:

```typescript
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
})

```

## Line by line explanation:

`import { defineConfig } from 'vite'` - imports helper function used to create a typed Vite configuration (better TypeScript support and autocomplete)

`import react from '@vitejs/plugin-react'` - imports react plugin for Vite

***plugin** - **an extension for Vite** that adds new functionalities (e.g. `@vitejs/plugin-react` adds support for React features)

`// https://vite.dev/config/` - comment with a link to official Vite configuration documentation (comments are ignored during execution)

`export default defineConfig({...})` - exports the configuration object that Vite will read when starting the development server or building the project

- `export default` - means this file exports **one main value**
- Vite automatically loads this file and uses the exported object as its configuration

`plugins: [react()]` - defines a list of plugins used by Vite
- `plugins` → configuration field where Vite plugins are registered
- `[...]` → plugins are stored in **array** because multiple plugins can be used

example:

```typescript
plugins: [react(), someOtherPlugin()]
```

`react()` - executes the React plugin and adds it to Vite.

The plugin enables:
- React JSX/TSX transformation
- React Fast Refresh
- React-specific optimizations

---

## What happens without `react()` plugin (theory)

The application may still work because:
- Vite uses **esbuild**
- esbuild can compile **TypeScript and JSX**
- modern browsers can run the generated JavaScript

However some React developer features will not work correctly.

---
---

### Observed difference (experiment no 1 – state preservation on component update)

Test:

1. click the counter button several times
2. modify the component text in `App.tsx`
3. save the file

### With `react()` plugin
React **Fast Refresh** is used.

Result:
- component updates instantly
- **state is preserved**

Example:

1. Initial state (before editing component)

![Count-with-react-plugin-before-refresh](../src/assets/documentation/configFiles/viteConfigTs/experiment1/Count-with-react-plugin-before-refresh.png)

2. Final state (after editing component)

![Count-with-react-plugin-after-refresh](../src/assets/documentation/configFiles/viteConfigTs/experiment1/Count-with-react-plugin-after-refresh.png)

State **does not reset**.

---

### Without `react()` plugin

Only Vite **HMR (Hot Module Replacement)** works:

Result:

- component reloads
- **state resets to initial value**

Example:

1. Initial state (before editing component)

![Count-without-react-plugin-before-refresh](../src/assets/documentation/configFiles/viteConfigTs/experiment1/Count-without-react-plugin-before-refresh.png)

2. Final state (after editing component)

![Count-without-react-plugin-after-refresh](../src/assets/documentation/configFiles/viteConfigTs/experiment1/Count-without-react-plugin-after-refresh.png)

State **resets**.

---
---

### Observed difference (experiment no. 2 – changing hook structure)

Test:

1. add a second React hook
2. increase the counter
3. remove (or comment out) the second hook
4. save the file

---

### With `react()` plugin

React **Fast Refresh** detects that the structure of hooks has changed.

Result:

- component is **remounted**
- **state resets to initial value**

Example:

1. Initial state (before changing hooks)

![hooks-initial-state](../src/assets/documentation/configFilesviteConfigTs/experiment2/hooks-initial-state.png)

2. Component code with two hooks

![hooks-added-hook](../src/assets/documentation/configFiles/viteConfigTs/experiment2/hooks-added-code.png)

3. Code after removing the second hook

![hooks-count-increased](../src/assets/documentation/configFiles/viteConfigTs/experiment2/hooks-count-increased.png)

4. Final state (after saving file)

![hooks-after-remount](../src/assets/documentation/configFiles/viteConfigTs/experiment2/hooks-after-remount.png)

State **resets** because React Fast Refresh cannot safely preserve state when the number or order of hooks changes.

---

### Explanation

React identifies hooks **by their order**, not by their variable names.

Example with two hooks:

1. → `useState(count)`
2. → `useState(firstName)`

When the second hook is removed:

1. → `useState(count)`

Because the hook structure changed, React can no longer reliably match the previous state values to the correct hooks.

To avoid inconsistent state, **React Fast Refresh forces a component remount**, which resets the state to its initial values.

### Without `react()` plugin

Without the React plugin, Vite falls back to standard **Hot Module Replacement (HMR)**.

Result:
- the component reloads
- **state resets to the initial value**

However, the mechanism is different from React Fast Refresh.

In this case:

- Vite replaces the updated module
- the React component is re-executed
- React initializes its state again

This means that the state reset is not a controlled safety mechanism (like with Fast Refresh), but simply a result of the component being reloaded.

---
---

### Observed difference (experiment no. 3 – changing component logic)

Test:

1. click the counter button several times
2. modify the component rendering logic
3. save the file

---

### With `react()` plugin

React **Fast Refresh** detects that the hook structure has not changed.

Result:

- the component updates instantly
- **state is preserved**

Example:

1. Initial state (before editing component logic)

![logic-before-change](../src/assets/documentation/configFiles/viteConfigTs/experiment3/logic-before-change.png)

2. Component code after changing the rendering logic

![logic-code-change](../src/assets/documentation/configFiles/viteConfigTs/experiment3/logic-code-change.png)

3. Final state (after saving file)

![logic-after-change](../src/assets/documentation/configFiles/viteConfigTs/experiment3/logic-after-change.png)

The state **does not reset** because React Fast Refresh can safely update the component when the hook structure remains unchanged.

---

### Without `react()` plugin

Without the React plugin, Vite uses standard **Hot Module Replacement (HMR)**.

Result:

- the component reloads
- **state resets to the initial value**

Example:

1. Initial state (before editing component logic)

![logic-before-change-no-plugin](../src/assets/documentation/configFiles/viteConfigTs/experiment3/logic-before-change-no-plugin.png)

2. Final state (after saving file)

![logic-after-change-no-plugin](../src/assets/documentation/configFiles/viteConfigTs/experiment3/logic-after-change-no-plugin.png)

State **resets** because the component module is reloaded and React initializes the state again.

## When state is preserved 

React Fast Refresh preserves state when: 
- the component hook order does not change 
- only JSX or rendering logic is modified 
- component structure remains compatible


---

## Key concept

| Feature                     | With React plugin | Without plugin  |
|-----------------------------|-------------------|-----------------|
| Vite HMR                    | ✅                | ✅             |
| React Fast Refresh          | ✅                | ❌             |
| State preservation on edit  | ✅                | ❌             |


## Why React Fast Refresh can preserve state

React Fast Refresh preserves component state only when the **hook order remains the same**.

React internally tracks hooks by the **order in which they are called during rendering**.

If the order of hooks changes, React can no longer correctly match previous state values with the correct hooks.

In that case, React performs a **forced component remount** to avoid inconsistent state.

---

## Summary
The React plugin is not required for the application to run, but it provides a better developer experience by enabling **React Fast Refresh**, which allows components to update without losing their internal state.
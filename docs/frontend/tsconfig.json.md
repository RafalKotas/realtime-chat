# tsconfig.json.md

Root TypeScript configuration file.

This file does not define compiler options directly.
Instead, it **connects multiple TypeScript configurations** into one project.

---

## Purpose

This file acts as a **project coordinator**.

It tells TypeScript:

> "this project consists of multiple sub-configurations"


In this case:

- frontend app (`tsconfig.app.json`)
- Node/Vite config (`tsconfig.node.json`)

---

## Structure

```
{ 
    "files": [], 
    "references": [ 
        { "path": "./tsconfig.app.json" }, 
        { "path": "./tsconfig.node.json" } 
    ] 
}
```

---

### files

`"files": []`

> Explicit list of files to include

Here it is empty → meaning:
- this config does NOT directly compile any files

It delegates everything to referenced configs.

---

### references

```
    "references": [ 
        { "path": "./tsconfig.app.json" }, 
        { "path": "./tsconfig.node.json" } 
    ] 
```

> Defines project references.

This is the key part.

---

## What are "references"?

References allow TypeScript to:
- split project into smaller parts
- build them independently
- understand dependencies between them

---

## In this project

1. `tsconfig.app.json`
- handles React app (`src/`)
- browser environment

---

2. `tsconfig.node.json`
- handles Vite config (`vite.config.ts`)
- Node environment

---

Root config connects them into one system.

---

## How TypeScript uses this

When running

`tsc --build`

TypeScript will:

1. Read `tsconfig.json`
2. See references
3. Process each config separately
4. Treat them as one project

---

## Why this is useful?

### Separation of concerns
- frontend config ≠ node config
- different environments
- different rules

---

### Better performance
- incremental builds
- caching per config

---

### Scalability

Later you can add:

`{ "path": "./tsconfig.test.json" }`
`{ "path": "./tsconfig.e2e.json" }`

---

## Important note

This setup is mainly used with:

`tsc --build`

But in Vite projects:
- Vite handles build
- TypeScript is used mainly for type checking

--- 

## Mental model

Think of it like:

- `tsconfig.json` → manager
- `tsconfig.app.json` → frontend worker
- `tsconfig.node.json` → tooling worker

---

## Summary

This file:
- does not compile code itself
- links multiple TypeScript configs
- enables modular project structure
- prepares project for scaling

It is a **project orchestrator**, not a compiler config.
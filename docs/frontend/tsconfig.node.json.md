# tsconfig.app.json

TypeScript configuration for Node.js-related files in the project (mainly Vite config).

---

## Purpose

This config is used for files that run in a **Node.js environment**, not in the browser.

In this project:
- `vite.config.ts`

---

## Code explanation

### compilerOptions

`"tsBuildInfoFile": "./node_modules/.tmp/tsconfig.app.tsbuildinfo"`

> Cache file for your TypeScript compiler. Allows for faster recompilations. 
> Saved in node_modules, so it doesn't go to the repo.

[Typescriptlang docs - tsBuildInfoFile](https://www.typescriptlang.org/tsconfig/#tsBuildInfoFile)

#### target

`"target": "ES2023"`

Specifies the JavaScript version output.

> ES2023 = modern JS (e.g. `Array.findLast`, `toSorted`)

[Typescriptlang docs - target](https://www.typescriptlang.org/tsconfig/#target)

#### lib

`"lib": ["ES2023"]

> Available APIs.

** Key difference vs app config: **
- no `DOM`
- no `DOM.Iterable`
- only JavaScript standard

Because Node.js does not have browser APIs like window or document.

[Typescriptlang docs - lib](https://www.typescriptlang.org/tsconfig/#lib)

---

#### module

`"module": "ESNext"`

> Uses modern ES modules (`import/export`).

[Typescriptlang docs - module](https://www.typescriptlang.org/tsconfig/#module)

---

#### types

`"types": ["node"]`

> Adds Node.js types.

Examples:

- `process`
- `__dirname`
- `fs`

Without this, TypeScript would not recognize Node globals.

[Typescriptlang docs - types](https://www.typescriptlang.org/tsconfig/#types)

---

#### skipLibCheck

`"skipLibCheck": true`

> Skips type checking of external libraries.

[Typescriptlang docs - skipLibCheck](https://www.typescriptlang.org/tsconfig/#skipLibCheck)

### Bundler mode

Same behavior as in `tsconfig.app.json`:

`"moduleResolution": "bundler"`

> Module resolution adapted for Vite.

[Typescriptlang docs - moduleResolution](https://www.typescriptlang.org/tsconfig/#moduleResolution)

---

#### allowImportingTsExtensions

`"allowImportingTsExtensions": true`

> Allows importing `.ts` files.

[Typescriptlang docs - allowImportingTsExtensions](https://www.typescriptlang.org/tsconfig/#allowImportingTsExtensions)

---

#### verbatimModuleSyntax

`"verbatimModuleSyntax": true`

> Keeps imports exactly as written.

[Typescriptlang docs - verbatimModuleSyntax](https://www.typescriptlang.org/tsconfig/#verbatimModuleSyntax)

---

#### moduleDetection

`"moduleDetection": "force"`

> Forces files to be treated as modules.

[Typescriptlang docs - moduleDetection](https://www.typescriptlang.org/tsconfig/#moduleDetection)

---

#### noEmit

`"noEmit": true`

> TypeScript does not generate `.js`.

[Typescriptlang docs - noEmit](https://www.typescriptlang.org/tsconfig/#noEmit)

Vite handles the build.

### Linting / code quality

Same strict rules as frontend config:

---

#### strict

`"strict": true`

> Enables strict type checking.

According to [strict - docs](https://www.typescriptlang.org/tsconfig/#strict):

---

#### noUnusedLocals

`"noUnusedLocals": true`

> Error if a variable is declared but never used.

[Typescriptlang docs - noUnusedLocals](https://www.typescriptlang.org/tsconfig/#noUnusedLocals)

---

#### erasableSyntaxOnly

`"erasableSyntaxOnly": true`

> Limits to syntax removable at compile time.

[Typescriptlang docs - erasableSyntaxOnly](https://www.typescriptlang.org/tsconfig/#erasableSyntaxOnly)

---

#### noFallthroughCasesInSwitch

`"noFallthroughCasesInSwitch": true`

> Prevents fallthrough in switch statements.

[Typescriptlang docs - noFallthroughCasesInSwitch](https://www.typescriptlang.org/tsconfig/#noFallthroughCasesInSwitch

---

#### noUncheckedSideEffectImports

`"noUncheckedSideEffectImports": true`

> Enforces explicit side-effect imports.

---

### include

`"include": ["vite.config.ts"]`

> TypeScript only analyzes the Vite config file.

---

## Key differences vs `tsconfig.app.json`

| Feature     | app (React)  | node (Vite config) |
|-------------|--------------|--------------------|
| Environment | Browser      | Node.js            |
| `lib`       | ES2023 + DOM | ES2023 only        |
| `types`     | vite/client  | node               |
| `JSX`       | yes          | no             |
| Files       | src/         | vite.config.ts     |

## Summary

This file:

- configures TypeScript for Node.js environment
- is used mainly for Vite configuration
- excludes browser APIs (no DOM)
- enables Node-specific types
- keeps the same strict quality rules as the frontend config

Think of it as:

- `tsconfig.app.json` → browser (React)
- `tsconfig.node.json` → Node (tools / config)
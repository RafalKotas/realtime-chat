# tsconfig.app.json

TypeScript configuration for front end application (`React` + `Vite`)

---

## Code explanation

### compilerOptions

#### tsBuildInfoFile

`"tsBuildInfoFile": "./node_modules/.tmp/tsconfig.app.tsbuildinfo"`

> Cache file for your TypeScript compiler. Allows for faster recompilations. 
> Saved in node_modules, so it doesn't go to the repo.

[Typescriptlang docs - tsBuildInfoFile](https://www.typescriptlang.org/tsconfig/#tsBuildInfoFile)

---

#### target

`"target": "ES2023"`

Specifies which JavaScript version the code is compiled to.

> ES2023 = modern JS (e.g. `Array.findLast`, `toSorted`)

[Typescriptlang docs - target](https://www.typescriptlang.org/tsconfig/#target)

---

#### useDefineForClassFields

`"useDefineForClassFields": true`

> Changes the way class fields work (according to the JS standard).

According to [useDefineForClassFields docs](https://www.typescriptlang.org/tsconfig/#useDefineForClassFields):

> This flag is used as part of migrating to the upcoming standard version of class fields. TypeScript introduced class fields many years before it was ratified in TC39. The latest version of the upcoming specification has a different runtime behavior to TypeScript’s implementation but the same syntax.
> This flag switches to the upcoming ECMA runtime behavior.

---

#### lib

`"lib": ["ES2023", "DOM", "DOM.Iterable"]`

Specifies the available APIs:

> ES2023 → modern JavaScript
> DOM → browser (document, window)
> DOM.Iterable → e.g., iterating over a NodeList

[Typescriptlang docs - lib](https://www.typescriptlang.org/tsconfig/#lib)

---

#### module

`"module": "ESNext"`

Module system.

> ESNext = modern `import/export`

[Typescriptlang docs - module](https://www.typescriptlang.org/tsconfig/#module)

---

#### types

`"types": ["vite/client"]`

Adds types for Vite:

> e.g., `import.meta.env`

[Typescriptlang docs - types](https://www.typescriptlang.org/tsconfig/#types)

---

#### skipLibCheck

`"skipLibCheck": true`

Skips type checking in libraries.

> - Speeds up build
> - Standard in most projects

[Typescriptlang docs - skipLibCheck](https://www.typescriptlang.org/tsconfig/#skipLibCheck)

---

### Bundler mode

#### moduleResolution

`"moduleResolution": "bundler"`

A method for finding modules.

> adapted to the bundler (Vite)

[Typescriptlang docs - moduleResolution](https://www.typescriptlang.org/tsconfig/#moduleResolution)

---

#### allowImportingTsExtensions

`"allowImportingTsExtensions": true`

> It allows you to import `.ts` files without problems.

[Typescriptlang docs - allowImportingTsExtensions](https://www.typescriptlang.org/tsconfig/#allowImportingTsExtensions)

---

#### verbatimModuleSyntax

`"verbatimModuleSyntax": true`

Doesn't change imports during compilation.

> keeps exactly what you wrote

[Typescriptlang docs - verbatimModuleSyntax](https://www.typescriptlang.org/tsconfig/#verbatimModuleSyntax)

---

#### moduleDetection

`"moduleDetection": "force"`

Forces files to be treated as modules.

[Typescriptlang docs - moduleDetection](https://www.typescriptlang.org/tsconfig/#moduleDetection)

---

#### noEmit

`"noEmit": true`

TypeScript does NOT generate `.js` files.

> because Vite does the build.

[Typescriptlang docs - noEmit](https://www.typescriptlang.org/tsconfig/#noEmit)

---

#### jsx

`"jsx": "react-jsx"`

JSX support for React (new system, no React import).

[Typescriptlang docs - jsx](https://www.typescriptlang.org/tsconfig/#jsx)

### Linting / code quality

---

#### strict

`"strict": true`

Enables strict type checking.

According to [strict - docs](https://www.typescriptlang.org/tsconfig/#strict):

> The strict flag enables a wide range of type checking behavior that results in stronger guarantees of program correctness. Turning this on is equivalent to enabling all of the strict mode family options, which are outlined below. You can then turn off individual strict mode family checks as needed.

[Typescriptlang docs - strict](https://www.typescriptlang.org/tsconfig/#strict)

---

#### noUnusedLocals

`"noUnusedLocals": true`

Report errors on unused local variables.

```javascript
const createKeyboard = (modelID: number) => {
  const defaultModelID = 23;
// 'defaultModelID' is declared but its value is never read.

  return { type: "keyboard", modelID };
};
```

[Typescriptlang docs - noUnusedLocals](https://www.typescriptlang.org/tsconfig/#noUnusedLocals)

---

#### noUnusedParameters

`"noUnusedParameters": true`

> Error if function parameter is not used.

```javascript
const createDefaultKeyboard = (modelID: number) => {
// 'modelID' is declared but its value is never read.

  const defaultModelID = 23;
  return { type: "keyboard", modelID: defaultModelID };
};
```

[Typescriptlang docs - noUnusedParameters](https://www.typescriptlang.org/tsconfig/#noUnusedParameters)

---

#### erasableSyntaxOnly

`"erasableSyntaxOnly": true`

Limits to syntax that can be safely removed at compile time.

> Less common, but OK in modern setups

[Typescriptlang docs - erasableSyntaxOnly](https://www.typescriptlang.org/tsconfig/#erasableSyntaxOnly)

---

#### noFallthroughCasesInSwitch

`"noFallthroughCasesInSwitch": true`

Prevents the switch statement from passing through non-empty cases.

[Typescriptlang docs - noFallthroughCasesInSwitch](https://www.typescriptlang.org/tsconfig/#noFallthroughCasesInSwitch)

---

#### noUncheckedSideEffectImports

`"noUncheckedSideEffectImports": true`

Enforces explicit imports with side effects.

> Increases code security

---

### include

`"include": ["src"]`

TypeScript only analyzes files in the src directory.

---

## Summary

This file:

- Defines how TypeScript analyzes React code
- Is optimized for Vite (bundler)
- Does not generate JS files (Vite does this)
- Enforces high code quality (strict + lint rules)


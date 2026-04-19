# package.json

**package.json** is used to store the metadata associated with the project as well as to store the list of dependency packages. In order to add dependency packages to your projects, you need to create **package.json** file. The file makes it easy for others to manage and install the packages associated with the project.

A **package.json** file:
- lists the packages your project depends on
- specifies versions of a package that your project can use
- makes your build reproducible, and therefore easier to share with other developers.

A **package.json** file may look similar to this:

```json
{
  "name": "realtimechatfe",
  "private": true,
  "version": "0.0.0",
  "type": "module",
  "scripts": {
    "dev": "vite",
    "build": "tsc -b && vite build",
    "lint": "eslint .",
    "preview": "vite preview"
  },
  "dependencies": {
    "react": "^19.2.4",
    "react-dom": "^19.2.4"
  },
  "devDependencies": {
    "@eslint/js": "^9.39.4",
    "@types/node": "^24.12.0",
    "@types/react": "^19.2.14",
    "@types/react-dom": "^19.2.3",
    "@vitejs/plugin-react": "^6.0.0",
    "eslint": "^9.39.4",
    "eslint-plugin-react-hooks": "^7.0.1",
    "eslint-plugin-react-refresh": "^0.5.2",
    "globals": "^17.4.0",
    "typescript": "~5.9.3",
    "typescript-eslint": "^8.56.1",
    "vite": "^8.0.0"
  }
}
```

This file outlines all the settings for the React app.

Each of the attributes in the file has its importance in some way or the other.

- `name` is the name of your app, which you give while executing `create-react-app<name-of-application>`. You can give any name of your choice to the app, the only condition being is that it **should be in lowercase**. It may also **contain hyphens and underscores**
---
- `"private": true` is one of the most crucial attributes. The use is that if you set private as true in your package.json, then npm will refuse to publish it within npm ecosystem. This is a way to prevent the accidental publication of private repositories
---
- `version` is the current version of your app. The version field must be of the form x.x.x. By default, create-react-app initializes it as 0.1.0
---
- `"type": "module"` specifies that the project uses modern ES Modules syntax (`import` / `export`)
---
- `dependencies` contains all the required node modules and versions required for the application in production. In the snapshot above, it contains two dependencies, which allows us to use `react`, `react-dom`.

In the screenshot above, the react version is specified as `^19.2.4`, which means that npm will install the most recent *major* version matching `19.x.x`. In contrast if you see something like `~5.6.7` in **package.json*, it means that it will install the most recent *minor* version matching `5.6.x`.

In order to add a package under `dependencies`, while installing, use `--save`.

For example, `npm install <package-name> --save`

This lists the package by default under `dependencies` with its version number.

---

- `devDependencies` forms an important part of **package.json**. It lists out the packages required for `development and testing`.

In order to add a package to this list, while installing, use `--save-dev`.

For example, `npm install <package-name> --save-dev`

This way, it is added to the list of `devDependencies`.

Scoped packages:
- f.e. `"@eslint/js": "^9.39.4",`
- `@eslint` means sth like organisation/group/namespace inside npm (oficial ESLint package)
- created for preventing names conflicts
- groups packages, f.e.:
    - @angular/core
    - @angular/router
    - @angular/forms

Official vs community:
- `@types/*` → oficial TypeScript types
- `@vitejs/*` → oficial Vite plugins
- `@eslint/*` → oficial ESLint tools

---

- `scripts` section defines shortcuts for commonly used commands.

These scripts can be executed using:

`npm run <script_name>`

They simplify running development tools such as Vite, TypeScript, and ESLint.

| Script   | Command                | Description                       |
| -------- | -----------------------|---------------------------------- |
| dev      | `vite`                 | starts development server         |
| build    | `tsc -b && vite build` | builds the project for production |
| lint     | `eslint .`             | checks code quality and style     |
| preview  | `vite preview`         | previews the production build     |

## dev

`vite`:
- launches Vite dev server
- serves app locally (e.g. `localhost:5173`)
- turn on **Hot Module Replacement (HMR)**

---

## build

`tsc -b && vite build`

---

`tsc` - TypeScript compiler:
- checks types (`type checking`)
- compiles TypeScript → JavaScript

---

`-b` (build mode)

`tsc -b`:
- **build** mode
- used with bigger projects (e.g. with `tsconfig.json`)
- compiles project according to configuration

in practice: a complete TypeScript check

---

`tsc -b && vite build` - run the second command only if the first one is successful, so:
- if TypeScript have errors → build will stop
- if everything is OK → go further

---

`vite build`:
- creates a **production version of the application**
- bundles files (Rollup under the hood)
- optimizes code (minify, tree-shaking)
- saves result in `dist` folder

---

## lint

`eslint .`

### Components:
- `eslint` → tool for code analysis
- `.` → means "check the entire project"

### What it does:
- detects errors
- monitors code style
- checks REact rules (e.g. hooks)

--- 

## preview

`vite preview`

### What it does:
- starts a local server for **the built application**
- shows final build (from `dist` folder)

used after:
```
npm run build
npm run preview
```

---

## Typical workflow:

```bash
npm run dev      # development
npm run build    # production build
npm run preview  # preview build
npm run lint     # check code quality
```

---

## Key concept

Scripts in `package.json` are **shortcuts for terminal commands**.

Instead of running:

`vite build`

you run:

`npm run build`

---

Scripts use locally installed packages from `node_modules`, so tools like Vite or ESLint do not need to be installed globally.
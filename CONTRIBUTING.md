# Contributing: branching rules

Simple trunk-based flow: one long-lived branch (`main`) plus short-lived work branches.

## 1. `main`
- `main` always holds working, deployable code.
- Nobody pushes to `main` directly. Every change goes in through a pull request.

## 2. Work branches
- Create every branch from the latest `main`.
- Name: `<type>/KAN-<number>-<short-description>`, lowercase, words separated by hyphens.
  - `feature/` new functionality, e.g. `feature/KAN-12-booking-form`
  - `fix/` bug fixes, e.g. `fix/KAN-15-menu-on-safari`
  - `design/` design files and assets, e.g. `design/KAN-6-handoff`
  - `chore/` setup, config, dependencies, docs, e.g. `chore/KAN-3-astro-setup`
- No Jira ticket? Leave out the `KAN-<number>` part.
- Branches made by Claude start with `claude/` (the tool requires it); the same PR rules apply to them.
- One branch = one task. Keep it short-lived (a few days). Do not delete branches after merging; they stay as history.

## 3. Commits
- Message starts with the ticket: `KAN-12: Add booking form validation`.
- Short, present tense, says what changed.

## 4. Pull requests
- Open a PR into `main`, title like the commit: `KAN-12: Booking form`.
- Before merging: the build passes and the change has been reviewed.
- Merge with **Squash and merge**. Keep the branch (do not delete it after merging).
- If `main` moved on, update your branch from `main` before merging.

## 5. Releases and urgent fixes
- A release is a tag on `main`: `v1.0.0`, `v1.1.0`, ...
- An urgent fix is a normal `fix/` branch from `main` with the same PR flow.

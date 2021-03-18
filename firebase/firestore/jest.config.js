// ref. https://typescript-jp.gitbook.io/deep-dive/intro-1/jest
module.exports = {
  "testEnvironment": "node",
  "testMatch": [
    "**/__tests__/**/*.+(ts|tsx|js)",
    "**/?(*.)+(spec|test).+(ts|tsx|js)"
  ],
  "transform": {
    "^.+\\.(ts|tsx)$": "ts-jest"
  },
  "globals": {
    "ts-jest": {
      "tsconfig": "tsconfig.json"
    }
  },  
}
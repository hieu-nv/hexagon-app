#!/usr/bin/env bash
set -euo pipefail

# Sync skills, agents, instructions, and workflows from upstream source directories
# to the local .agent folder (and .github mirror) for Spring Boot + Kotlin hexagonal architecture projects.
#
# Usage: ./agent-skills-sync.sh [-f]
#   -f    Force copy, overwrite existing files

# Source directories (searched in order, first match wins)
SOURCE_DIRS=(
    "../../skills"
    "../../awesome-copilot"
    "../../antigravity-awesome-skills"
)

DEST_DIR="../.agent"
GITHUB_MIRROR="../.github"
FORCE=false

# Parse arguments
while getopts "f" opt; do
    case $opt in
        f) FORCE=true ;;
        *) echo "Usage: $0 [-f]" >&2; exit 1 ;;
    esac
done

# ─────────────────────────────────────────────
# Skills to copy
# ─────────────────────────────────────────────
COPY_SKILLS=(
    "api-design-principles"
    "api-security-best-practices"
    "api-security-testing"
    "architecture"
    "architecture-blueprint-generator"
    "architecture-patterns"
    "clean-code"
    "code-exemplars-blueprint-generator"
    "code-review-checklist"
    "code-review-excellence"
    "conventional-commit"
    "create-architectural-decision-record"
    "create-implementation-plan"
    "create-readme"
    "create-specification"
    "create-spring-boot-kotlin-project"
    "create-technical-spike"
    "database"
    "database-design"
    "database-optimizer"
    "datadog-automation"
    "ddd-context-mapping"
    "ddd-strategic-design"
    "ddd-tactical-patterns"
    "debugging-strategies"
    "debugging-toolkit-smart-debug"
    "deployment-engineer"
    "deployment-procedures"
    "devops-rollout-plan"
    "docker-expert"
    "documentation"
    "documentation-writer"
    "domain-driven-design"
    "editorconfig"
    "gh-cli"
    "git-advanced-workflows"
    "git-commit"
    "git-flow-branch-creator"
    "java-junit"
    "kotlin-springboot"
    "multi-stage-dockerfile"
    "postgresql-code-review"
    "postgresql-optimization"
    "refactor"
    "refactor-plan"
    "review-and-refactor"
    "security-audit"
    "security-auditor"
    "security-scanning-security-dependencies"
    "security-scanning-security-hardening"
    "sql-code-review"
    "sql-optimization"
    "sql-optimization-patterns"
    "sql-pro"
    "tdd-workflow"
    "tdd-workflows-tdd-cycle"
    "tdd-workflows-tdd-green"
    "tdd-workflows-tdd-red"
    "tdd-workflows-tdd-refactor"
    "technology-stack-blueprint-generator"
    "test-driven-development"
)

# ─────────────────────────────────────────────
# Agents to copy
# ─────────────────────────────────────────────
COPY_AGENTS=(
    "4.1-Beast.agent.md"
    "Thinking-Beast-Mode.agent.md"
    "Ultimate-Transparent-Thinking-Beast-Mode.agent.md"
    "api-architect.agent.md"
    "arch.agent.md"
    "context-architect.agent.md"
    "debug.agent.md"
    "github-actions-expert.agent.md"
    "kotlin-mcp-expert.agent.md"
    "openapi-to-application.agent.md"
    "playwright-tester.agent.md"
    "polyglot-test-builder.agent.md"
    "polyglot-test-fixer.agent.md"
    "polyglot-test-generator.agent.md"
    "polyglot-test-implementer.agent.md"
    "polyglot-test-linter.agent.md"
    "polyglot-test-planner.agent.md"
    "polyglot-test-researcher.agent.md"
    "polyglot-test-tester.agent.md"
    "repo-architect.agent.md"
    "research-technical-spike.agent.md"
    "se-gitops-ci-specialist.agent.md"
    "se-security-reviewer.agent.md"
    "se-system-architecture-reviewer.agent.md"
    "task-researcher.agent.md"
)

# ─────────────────────────────────────────────
# Instructions to copy
# ─────────────────────────────────────────────
COPY_INSTRUCTIONS=(
    "agent-safety.instructions.md"
    "agent-skills.instructions.md"
    "agents.instructions.md"
    "code-review-generic.instructions.md"
    "containerization-docker-best-practices.instructions.md"
    "context-engineering.instructions.md"
    "github-actions-ci-cd-best-practices.instructions.md"
    "kubernetes-deployment-best-practices.instructions.md"
    "kotlin-mcp-server.instructions.md"
    "security-and-owasp.instructions.md"
    "springboot.instructions.md"
)

# ─────────────────────────────────────────────
# Workflows to copy
# ─────────────────────────────────────────────
COPY_WORKFLOWS=(
    "daily-issues-report.md"
)

# ─────────────────────────────────────────────
# Helper: copy_items <src_subdir> <dest_subdir> <items_array>
# ─────────────────────────────────────────────
copy_items() {
    local src_subdir="$1"
    local dest_subdir="$2"
    shift 2
    local items=("$@")

    local dest_path="$DEST_DIR/$dest_subdir"
    local mirror_path="$GITHUB_MIRROR/$dest_subdir"
    mkdir -p "$dest_path" "$mirror_path"

    local copied=0 skipped=0 notfound=0

    for item in "${items[@]}"; do
        local dest_item="$dest_path/$item"
        local mirror_item="$mirror_path/$item"
        local source_item=""

        # Search source dirs
        for src_dir in "${SOURCE_DIRS[@]}"; do
            local candidate="$src_dir/$src_subdir/$item"
            if [[ -e "$candidate" ]]; then
                source_item="$candidate"
                break
            fi
            # Also try without subdir (e.g. skills/ at root level)
            local candidate2="$src_dir/$item"
            if [[ -e "$candidate2" ]]; then
                source_item="$candidate2"
                break
            fi
        done

        if [[ -z "$source_item" ]]; then
            echo "  ✗ Not found: $item"
            ((notfound++))
            continue
        fi

        if [[ -e "$dest_item" && "$FORCE" == false ]]; then
            echo "  ⊘ Skip:     $item"
            ((skipped++))
            continue
        fi

        # Remove if forcing
        [[ -e "$dest_item" && "$FORCE" == true ]] && rm -rf "$dest_item"
        [[ -e "$mirror_item" && "$FORCE" == true ]] && rm -rf "$mirror_item"

        cp -r "$source_item" "$dest_item"
        cp -r "$source_item" "$mirror_item"
        echo "  ✓ Copied:   $item"
        ((copied++))
    done

    echo "  → Copied: $copied | Skipped: $skipped | Not found: $notfound"
    echo ""
}

# ─────────────────────────────────────────────
# Check at least one source dir exists
# ─────────────────────────────────────────────
found_source=false
for src_dir in "${SOURCE_DIRS[@]}"; do
    if [[ -d "$src_dir" ]]; then
        found_source=true
        break
    fi
done

if [[ "$found_source" == false ]]; then
    echo "Error: No source directories found:"
    for src_dir in "${SOURCE_DIRS[@]}"; do echo "  - $src_dir"; done
    exit 1
fi

echo "=================================================="
echo "  Agent Sync — source → $DEST_DIR"
echo "  Mirror → $GITHUB_MIRROR"
echo "  Force: $FORCE"
echo "=================================================="
echo ""

echo "📦 SKILLS → skills/"
copy_items "skills" "skills" "${COPY_SKILLS[@]}"

echo "🤖 AGENTS → agents/"
copy_items "agents" "agents" "${COPY_AGENTS[@]}"

echo "📋 INSTRUCTIONS → instructions/"
copy_items "instructions" "instructions" "${COPY_INSTRUCTIONS[@]}"

echo "⚙️  WORKFLOWS → workflows/"
copy_items "workflows" "workflows" "${COPY_WORKFLOWS[@]}"

echo "=================================================="
echo "Sync complete!"
echo "=================================================="

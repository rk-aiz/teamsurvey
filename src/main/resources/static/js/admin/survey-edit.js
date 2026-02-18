/**
 * アンケート編集画面用スクリプト
 */

// 回答パターンのキャッシュデータ
let answerOptionCache = [];

// 回答パターンデータを取得してキャッシュする
function fetchAnswerOptions() {
    return fetch("/admin/pattern/fragment/list-json")
        .then((response) => {
            if (!response.ok) throw new Error("Network response was not ok");
            return response.json();
        })
        .then((data) => {
            answerOptionCache = data;
            return data;
        })
        .catch((error) =>
            console.error("Error fetching answer options:", error),
        );
}

// 選択された回答パターンのプレビューを更新する
function updatePatternPreview(selectElement) {
    const container = selectElement.closest(".pattern-container");
    if (!container) return;
    const previewDiv = container.querySelector(".pattern-preview");
    if (!previewDiv) return;

    const selectedId = parseInt(selectElement.value);
    const option = answerOptionCache.find(
        (opt) => opt.answerOptionId === selectedId,
    );

    if (option && option.items && option.items.length > 0) {
        const itemsText = option.items.map((item) => item.itemText).join(" / ");
        previewDiv.textContent = "選択肢: " + itemsText;
    } else {
        previewDiv.textContent = "";
    }
}

// 設問アイテムのイベントリスナーを初期化する
function initQuestionItem(item) {
    // name属性に .type や .answerOption.answerOptionId が含まれるselect要素を取得
    const typeSelect = item.querySelector('select[name*=".type"]');
    const patternSelect = item.querySelector('select[name*=".answerOptionId"]');
    const patternContainer = item.querySelector(".pattern-container");

    if (typeSelect && patternSelect && patternContainer) {
        function updatePatternState() {
            if (typeSelect.disabled) return;
            if (typeSelect.value === "TEXT") {
                patternSelect.value = "";
                patternContainer.style.display = "none";
            } else {
                patternContainer.style.display = "block";
            }
        }
        typeSelect.addEventListener("change", updatePatternState);
        updatePatternState();

        patternSelect.addEventListener("change", function () {
            updatePatternPreview(this);
        });
    }
}

document.addEventListener("DOMContentLoaded", function () {
    // --- 対象グループ設定関連 ---
    const groupCheckboxes = document.querySelectorAll(
        '#groupSelectionModal input[type="checkbox"]',
    );
    const displayContainer = document.getElementById("selectedGroupsDisplay");

    function updateGroupDisplay() {
        if (!displayContainer) return;

        displayContainer.innerHTML = "";
        let selectedCount = 0;

        groupCheckboxes.forEach((cb) => {
            if (cb.checked) {
                selectedCount++;
                const label = document.querySelector(
                    'label[for="' + cb.id + '"]',
                );
                if (label) {
                    const badge = document.createElement("span");
                    badge.className = "badge bg-primary me-1";
                    badge.textContent = label.textContent;
                    displayContainer.appendChild(badge);
                }
            }
        });

        if (selectedCount === 0) {
            displayContainer.innerHTML =
                '<span class="text-muted small">未選択</span>';
        }
    }

    if (displayContainer) {
        updateGroupDisplay();
        groupCheckboxes.forEach((cb) => {
            cb.addEventListener("change", updateGroupDisplay);
        });
    }

    // --- 回答パターンデータのロードと初期表示 ---
    fetchAnswerOptions().then(() => {
        // ロード完了後、全てのプレビューを初期更新
        document
            .querySelectorAll('select[name*=".answerOptionId"]')
            .forEach((select) => {
                updatePatternPreview(select);
            });
    });

    // --- 設問タイプと回答パターンの連動 ---
    // 既存の設問に対して初期化を実行
    document.querySelectorAll(".question-item").forEach(initQuestionItem);

    // --- Offcanvas関連 ---
    const patternEditor = document.getElementById("patternEditor");
    if (patternEditor) {
        patternEditor.addEventListener("show.bs.offcanvas", function () {
            loadPatternList();
        });
    }
});

// --- 回答パターン編集用 AJAX関数 ---

// 一覧ロード
function loadPatternList() {
    const editorContent = document.getElementById("patternEditorContent");
    if (!editorContent) return;

    fetch("/admin/pattern/fragment/list")
        .then((response) => response.text())
        .then((html) => {
            editorContent.innerHTML = html;
        });
}

// フォームロード (新規 or 編集)
function loadPatternForm(id) {
    const editorContent = document.getElementById("patternEditorContent");
    if (!editorContent) return;

    let url = "/admin/pattern/fragment/form";
    if (id) {
        url += "?id=" + id;
    }
    fetch(url)
        .then((response) => response.text())
        .then((html) => {
            editorContent.innerHTML = html;
            initSortable();
        });
}

// 保存処理
function submitPatternForm(event) {
    event.preventDefault();
    const editorContent = document.getElementById("patternEditorContent");
    const form = event.target;
    const formData = new FormData(form);

    // FormData -> JSON 変換 (ネストされた items[] に対応)
    const data = {};
    const items = [];

    formData.forEach((value, key) => {
        // items[index].field の形式を解析
        const itemMatch = key.match(/^items\[(\d+)\]\.(.+)$/);
        if (itemMatch) {
            const index = parseInt(itemMatch[1]);
            const field = itemMatch[2];
            if (!items[index]) items[index] = {};
            items[index][field] = value;
        } else {
            data[key] = value;
        }
    });

    // 配列の空要素を詰めてセット
    data.items = items.filter((i) => i !== undefined && i !== null);

    // 数値型への変換 (必要に応じて)
    if (data.id) data.id = parseInt(data.id);
    data.items.forEach((item) => {
        if (item.itemId) item.itemId = parseInt(item.itemId);
        if (item.itemOrder) item.itemOrder = parseInt(item.itemOrder);
    });

    // CSRFトークンの取得
    const token = document.querySelector('meta[name="_csrf"]')?.content;
    const header = document.querySelector('meta[name="_csrf_header"]')?.content;
    const headers = { "Content-Type": "application/json" };
    if (token && header) {
        headers[header] = token;
    }

    fetch("/admin/pattern/fragment/save", {
        method: "POST",
        headers: headers,
        body: JSON.stringify(data),
    }).then(async (response) => {
        if (response.ok) {
            // 成功時: 一覧を再ロードしてメイン画面も更新
            loadPatternList();
            refreshMainSelectBoxes();
        } else {
            // エラー時: バリデーションエラーを表示
            const errors = await response.json();

            // 既存のエラー表示をクリア
            form.querySelectorAll(".is-invalid").forEach((el) =>
                el.classList.remove("is-invalid"),
            );
            form.querySelectorAll(".invalid-feedback").forEach(
                (el) => (el.textContent = ""),
            );

            // エラーメッセージの反映
            Object.keys(errors).forEach((field) => {
                // name="items[0].itemText" のようなフィールドを検索
                const input = form.querySelector(`[name="${field}"]`);
                if (input) {
                    input.classList.add("is-invalid");
                    // inputの直後、あるいは親要素内の .invalid-feedback を探す
                    const feedback =
                        input.parentElement.querySelector(
                            ".invalid-feedback",
                        ) || input.nextElementSibling;
                    if (feedback) {
                        feedback.textContent = errors[field];
                    }
                }
            });
        }
    });
}

// メイン画面のセレクトボックスを更新する(ページリロードなしで反映)
function refreshMainSelectBoxes() {
    fetchAnswerOptions().then((data) => {
        if (!data) return;
        const selects = document.querySelectorAll(
            'select[name*=".answerOptionId"]',
        );
        selects.forEach((select) => {
            const currentVal = select.value;
            select.innerHTML =
                '<option value="">-- 選択してください --</option>';

            data.forEach((opt) => {
                const option = document.createElement("option");
                option.value = opt.answerOptionId;
                option.textContent = opt.name;
                select.appendChild(option);
            });

            if (currentVal) select.value = currentVal;

            // プレビューも更新
            updatePatternPreview(select);
        });
    });
}

// --- SortableJS 初期化 ---
function initSortable() {
    const el = document.getElementById("pattern-items-container");
    if (el && typeof Sortable !== "undefined") {
        new Sortable(el, {
            handle: ".handle", // ドラッグハンドルのみで掴めるようにする
            animation: 150,
            onEnd: function () {
                updatePatternItemIndices(); // 並び替え後にインデックスを更新
            },
        });
    }
}

// --- 回答パターン選択肢編集用 JS ---

// 選択肢を追加
function addPatternItem() {
    const container = document.getElementById("pattern-items-container");
    const template = document.getElementById("pattern-item-template");

    if (container && template) {
        // テンプレートをクローン
        const clone = template.content.cloneNode(true);
        container.appendChild(clone);
        updatePatternItemIndices();
    }
}

// 選択肢を削除
function removePatternItem(btn) {
    const itemRow = btn.closest(".pattern-item-row");
    if (itemRow) {
        itemRow.remove();
        updatePatternItemIndices();
    }
}

// インデックスを振り直す (items[0], items[1]...)
function updatePatternItemIndices() {
    const container = document.getElementById("pattern-items-container");
    if (!container) return;

    const rows = container.querySelectorAll(".pattern-item-row");
    rows.forEach((row, index) => {
        // 行内のすべての input[name*="items"] を取得してインデックスを更新
        const inputs = row.querySelectorAll('input[name*="items"]');
        inputs.forEach((input) => {
            // name属性のインデックス部分を置換 items[?] -> items[index]
            input.name = input.name.replace(/items\[\d*\]/, `items[${index}]`);
        });

        // itemOrder の値を更新 (0始まりの連番)
        const orderInput = row.querySelector(".item-order");
        if (orderInput) {
            orderInput.value = index;
        }
    });
}

// --- 設問の並べ替え用スクリプト ---

function moveUp(btn) {
    const current = btn.closest(".question-item");
    const prev = current.previousElementSibling;
    // 前の要素があり、かつ質問ブロックであれば入れ替え
    if (prev && prev.classList.contains("question-item")) {
        current.parentNode.insertBefore(current, prev);
        updateIndices(); // インデックスを振り直す
    }
}

function moveDown(btn) {
    const current = btn.closest(".question-item");
    const next = current.nextElementSibling;
    // 次の要素があり、かつ質問ブロックであれば入れ替え
    if (next && next.classList.contains("question-item")) {
        current.parentNode.insertBefore(next, current);
        updateIndices(); // インデックスを振り直す
    }
}

// --- 設問の追加・削除用スクリプト ---

function addQuestion() {
    const container = document.getElementById("question-list");
    const template = document.getElementById("question-template");
    if (container && template) {
        const clone = template.content.cloneNode(true);
        // 追加された要素を取得するために、コンテナの最後の子要素として追加後に取得する手もあるが、
        // cloneはDocumentFragmentなので、追加前に要素を特定するのは少し手間。
        // ここでは追加後に updateIndices を呼び、最後の要素に対して initQuestionItem を呼ぶ。
        container.appendChild(clone);
        updateIndices();

        const newItems = container.querySelectorAll(".question-item");
        initQuestionItem(newItems[newItems.length - 1]);
    }
}

function removeQuestion(btn) {
    const item = btn.closest(".question-item");
    if (item) {
        item.remove();
        updateIndices();
    }
}

// Springが正しくバインドできるように name属性のインデックス[0], [1]... を振り直す関数
function updateIndices() {
    const items = document.querySelectorAll(".question-item");
    items.forEach((item, index) => {
        // 1. 表示上の番号 (Q1, Q2...) を更新
        const label = item.querySelector(".q-number");
        if (label) label.textContent = index + 1;

        // 2. すべての input/select の name属性 (questionForms[x].field) を更新
        const inputs = item.querySelectorAll('[name*="questionForms["]');
        inputs.forEach((input) => {
            const name = input.getAttribute("name");
            // 正規表現で questionForms[数字] または questionForms[] を新しい数字に置換
            const newName = name.replace(
                /questionForms\[\d*\]/,
                `questionForms[${index}]`,
            );
            input.setAttribute("name", newName);
        });

        // 3. IDとLabelの整合性 (q_req_0, q_text_0, q_type_0, ans_pattern_0)
        // 正規表現で末尾の _数字 または _TEMPLATE を _index に置換する
        const idElements = item.querySelectorAll(
            '[id^="q_"], [id^="ans_pattern_"]',
        );
        idElements.forEach((el) => {
            el.id = el.id
                .replace(/_\d+$/, `_${index}`)
                .replace(/_TEMPLATE$/, `_${index}`);
        });

        const labels = item.querySelectorAll(
            'label[for^="q_"], label[for^="ans_pattern_"]',
        );
        labels.forEach((label) => {
            const forAttr = label.getAttribute("for");
            label.setAttribute(
                "for",
                forAttr
                    .replace(/_\d+$/, `_${index}`)
                    .replace(/_TEMPLATE$/, `_${index}`),
            );
        });
    });
}

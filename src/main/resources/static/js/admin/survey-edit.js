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
    const questionItems = document.querySelectorAll(".question-item");
    questionItems.forEach(function (item) {
        // name属性に .type や .answerOption.answerOptionId が含まれるselect要素を取得
        const typeSelect = item.querySelector('select[name*=".type"]');
        const patternSelect = item.querySelector(
            'select[name*=".answerOptionId"]',
        );
        const patternContainer = item.querySelector(".pattern-container");

        if (typeSelect && patternSelect && patternContainer) {
            function updatePatternState() {
                // アンケートが公開中などで編集不可(disabled)の場合はJSで操作しない
                if (typeSelect.disabled) return;

                if (typeSelect.value === "TEXT") {
                    patternSelect.value = ""; // パターンなし(空)に強制変更
                    patternContainer.style.display = "none"; // 非表示
                } else {
                    patternContainer.style.display = "block"; // 表示
                }
            }
            typeSelect.addEventListener("change", updatePatternState);
            // 初期表示時にも適用
            updatePatternState();

            // 回答パターン変更時にプレビューを更新
            patternSelect.addEventListener("change", function () {
                updatePatternPreview(this);
            });
        }
    });

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

    // CSRFトークンの取得
    const token = document.querySelector('meta[name="_csrf"]')?.content;
    const header = document.querySelector('meta[name="_csrf_header"]')?.content;
    const headers = {};
    if (token && header) {
        headers[header] = token;
    }

    fetch("/admin/pattern/fragment/save", {
        method: "POST",
        headers: headers,
        body: formData,
    })
        .then((response) => response.text())
        .then((html) => {
            // レスポンスが一覧フラグメントかフォームフラグメント(エラー時)かで分岐したいが、
            // 簡易的にHTMLを入れ替えて、フォームがなければ成功とみなす
            editorContent.innerHTML = html;

            // 保存成功（一覧画面に戻った）場合、メイン画面のセレクトボックスを更新する
            if (!editorContent.querySelector("form")) {
                refreshMainSelectBoxes();
            } else {
                // エラー等でフォームが再表示された場合もSortableを有効化
                initSortable();
            }
        });
}

// メイン画面のセレクトボックスを更新する（ページリロードなしで反映）
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

// Springが正しくバインドできるように name属性のインデックス[0], [1]... を振り直す関数
function updateIndices() {
    const items = document.querySelectorAll(".question-item");
    items.forEach((item, index) => {
        // 1. 表示上の番号 (Q1, Q2...) を更新
        const label = item.querySelector(".q-number");
        if (label) label.textContent = index + 1;

        // 2. 削除ボタンの value (サーバー側での削除用インデックス) を更新
        const removeBtn = item.querySelector('button[name="removeQuestion"]');
        if (removeBtn) removeBtn.value = index;

        // 3. すべての input/select の name属性 (questionForms[x].field) を更新
        const inputs = item.querySelectorAll('[name*="questionForms["]');
        inputs.forEach((input) => {
            const name = input.getAttribute("name");
            // 正規表現で questionForms[数字] を新しい数字に置換
            const newName = name.replace(
                /questionForms\[\d+\]/,
                `questionForms[${index}]`,
            );
            input.setAttribute("name", newName);
        });
    });
}

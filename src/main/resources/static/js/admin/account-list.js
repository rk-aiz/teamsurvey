/**
 * アカウント一覧画面用スクリプト
 */
document.addEventListener("DOMContentLoaded", () => {
    // 1. イベントリスナーの登録 (他の処理でエラーが出ても動くように先に実行)

    // Ajaxでのフォーム送信処理 (Event Delegation)
    document.addEventListener("submit", function (e) {
        if (e.target?.id === "accountForm") {
            e.preventDefault(); // 通常の送信をキャンセル

            const form = e.target;
            const formData = new FormData(form);

            // FormData -> JSON Object 変換
            const data = {};
            formData.forEach((value, key) => {
                // Springのフォーム用hiddenフィールド（_で始まるもの）は除外
                if (key.startsWith("_")) return;

                // groupIds は配列として処理 (accountForm用)
                if (key === "groupIds") {
                    if (!data[key]) {
                        data[key] = [];
                    }
                    data[key].push(parseInt(value));
                } else if (
                    key === "enabled" ||
                    key === "isNew" ||
                    key === "isSystemGroup"
                ) {
                    // booleanフィールドの変換 (文字列 "true" は true に、それ以外は false)
                    data[key] = value === "true";
                } else {
                    data[key] = value;
                }
            });

            // チェックボックス(enabled)が未チェックの場合、FormDataに含まれないため明示的にfalseを設定
            if (
                form.querySelector('input[name="enabled"]') &&
                !("enabled" in data)
            ) {
                data["enabled"] = false;
            }

            // CSRFトークンの取得
            const token = document.querySelector('meta[name="_csrf"]')?.content;
            const header = document.querySelector(
                'meta[name="_csrf_header"]',
            )?.content;
            const headers = { "Content-Type": "application/json" };
            if (token && header) {
                headers[header] = token;
            }

            fetch(form.action, {
                method: "POST",
                headers: headers,
                body: JSON.stringify(data),
            })
                .then(async (response) => {
                    if (response.ok) {
                        // 成功時: メッセージを保存してリロード
                        const resData = await response.json();
                        sessionStorage.setItem(
                            "actionMessage",
                            resData.message,
                        );
                        window.location.href = window.location.pathname;
                    } else {
                        // エラー時: JSONをパースして表示
                        const errors = await response.json();

                        // 既存のエラー表示をクリア
                        form.querySelectorAll(".is-invalid").forEach((el) =>
                            el.classList.remove("is-invalid"),
                        );
                        form.querySelectorAll(".invalid-feedback").forEach(
                            (el) => (el.textContent = ""),
                        );

                        // エラーメッセージの表示
                        Object.keys(errors).forEach((field) => {
                            const input = form.querySelector(
                                `[name="${field}"]`,
                            );
                            if (input) {
                                input.classList.add("is-invalid");
                                const feedback =
                                    input.parentElement.querySelector(
                                        ".invalid-feedback",
                                    );
                                if (feedback) {
                                    feedback.textContent = errors[field];
                                }
                            } else if (field === "errorMessage") {
                                // その他のサーバーエラー
                                alert(errors[field]);
                            }
                        });
                    }
                })
                .catch((error) => console.error("Error:", error));
        }
    });

    // 2. モーダル表示制御 (Bootstrap依存)
    try {
        const mainElement = document.querySelector("main[data-show-modal]");
        const accountModalEl = document.getElementById("accountModal");

        if (mainElement && accountModalEl) {
            const showModal = mainElement.dataset.showModal === "true";

            if (showModal && typeof bootstrap !== "undefined") {
                const myModal = new bootstrap.Modal(accountModalEl);
                myModal.show();

                // モーダルが閉じられた時に一覧に戻る（URLパラメータクリア）
                accountModalEl.addEventListener("hidden.bs.modal", function () {
                    // 現在のパス（クエリパラメータなし）を取得して遷移
                    const path = window.location.pathname;
                    window.location.href = path;
                });
            }
        }
    } catch (error) {
        console.error("Modal initialization failed:", error);
    }

    // 3. フラッシュメッセージの表示（リロード後の表示用）
    const actionMessage = sessionStorage.getItem("actionMessage");
    if (actionMessage) {
        const container = document.getElementById("flash-message-container");
        if (container) {
            const alertDiv = document.createElement("div");
            alertDiv.className = "alert alert-success";
            alertDiv.textContent = actionMessage;
            container.innerHTML = ""; // 既存の内容をクリアして差し替え
            container.appendChild(alertDiv);
        }
        sessionStorage.removeItem("actionMessage");
    }

    // グループ編集ボタンの処理
    document.addEventListener("click", function (e) {
        const btn = e.target.closest(".edit-group-btn");
        if (btn) {
            const id = btn.dataset.groupId;
            const name = btn.dataset.groupName;
            const authority = btn.dataset.authority;
            const isSystemGroup = btn.dataset.isSystemGroup === "true";

            // フォームに値をセット
            const form = document.getElementById("groupForm");
            form.querySelector('input[name="id"]').value = id;
            form.querySelector('input[name="groupName"]').value = name;
            const authSelect = form.querySelector('select[name="authority"]');
            if (authSelect) {
                authSelect.value = authority;
                // システムグループの場合は操作不可にする（値は送信される）
                if (isSystemGroup) {
                    authSelect.style.pointerEvents = "none";
                    authSelect.style.backgroundColor = "#e9ecef"; // Bootstrap disabled color
                    authSelect.setAttribute("tabindex", "-1"); // フォーカス移動も防ぐ
                } else {
                    authSelect.style.pointerEvents = "";
                    authSelect.style.backgroundColor = "";
                    authSelect.removeAttribute("tabindex");
                }
            }

            // UI変更
            document.querySelector("#groupFormTitle").textContent =
                "グループ編集";
            document.querySelector(
                "#groupFormSubmitBtn .btn-text",
            ).textContent = "更新";
            document.querySelector("#groupFormSubmitBtn i").className =
                "bi bi-save me-1";
            document
                .getElementById("groupFormCancelBtn")
                .classList.remove("d-none");

            // フォームへスクロール
            document
                .getElementById("groupForm")
                .scrollIntoView({ behavior: "smooth", block: "center" });
        }
    });

    // グループ編集キャンセルボタン
    const groupCancelBtn = document.getElementById("groupFormCancelBtn");
    if (groupCancelBtn) {
        groupCancelBtn.addEventListener("click", function () {
            const form = document.getElementById("groupForm");
            form.reset();
            form.querySelector('input[name="id"]').value = "";

            // 権限セレクトボックスのロック解除
            const authSelect = form.querySelector('select[name="authority"]');
            if (authSelect) {
                authSelect.style.pointerEvents = "";
                authSelect.style.backgroundColor = "";
                authSelect.removeAttribute("tabindex");
            }

            // UIを初期状態に戻す
            document.querySelector("#groupFormTitle").textContent =
                "新規グループ追加";
            document.querySelector(
                "#groupFormSubmitBtn .btn-text",
            ).textContent = "追加";
            document.querySelector("#groupFormSubmitBtn i").className =
                "bi bi-plus-lg me-1";
            this.classList.add("d-none");
            form.querySelectorAll(".is-invalid").forEach((el) =>
                el.classList.remove("is-invalid"),
            );
        });
    }

    // パスワード確認欄のアニメーション制御
    const passwordInput = document.querySelector(
        '#accountForm input[name="password"]',
    );
    const confirmCollapseEl = document.getElementById(
        "passwordConfirmationCollapse",
    );

    if (
        passwordInput &&
        confirmCollapseEl &&
        typeof bootstrap !== "undefined"
    ) {
        const confirmCollapse = new bootstrap.Collapse(confirmCollapseEl, {
            toggle: false,
        });

        const updateVisibility = () => {
            if (passwordInput.value.length > 0) {
                confirmCollapse.show();
            } else {
                confirmCollapse.hide();
            }
        };

        passwordInput.addEventListener("input", updateVisibility);
        // 初期状態の反映（バリデーションエラー時など値が入っている場合に対応）
        updateVisibility();
    }
});

/**
 * アンケート一覧画面用スクリプト
 * クライアントサイドでのフィルタリング機能を提供
 */
document.addEventListener("DOMContentLoaded", () => {
    const filterLinks = document.querySelectorAll("[data-filter-type]");
    const surveyItems = document.querySelectorAll(".survey-item");
    const noDataMessage = document.getElementById("no-surveys-message");
    const detailPane = document.getElementById("detail-pane");

    // URLパラメータから初期ステータスを取得
    const urlParams = new URLSearchParams(window.location.search);
    const initialStatus = urlParams.get("status") || "ALL";

    // 初期表示
    applyFilter(initialStatus);

    // フィルタボタンのクリックイベント
    filterLinks.forEach((link) => {
        link.addEventListener("click", (e) => {
            e.preventDefault();
            const status = link.dataset.filterType;

            applyFilter(status);
            updateUrl(status);
        });
    });

    // アンケート選択時の非同期読み込み処理
    surveyItems.forEach((item) => {
        item.addEventListener("click", (e) => {
            e.preventDefault();

            // 選択状態の見た目を更新
            surveyItems.forEach((i) =>
                i.classList.remove("list-group-item-primary"),
            );
            item.classList.add("list-group-item-primary");

            const url = item.href;

            // URLを更新 (ブラウザ履歴に追加)
            window.history.pushState({}, "", url);

            // 非同期でページを取得して右ペインのみ更新
            fetch(url)
                .then((response) => {
                    if (!response.ok)
                        throw new Error("Network response was not ok");
                    return response.text();
                })
                .then((html) => {
                    // 取得したHTMLから詳細ペイン部分だけを抽出
                    const parser = new DOMParser();
                    const doc = parser.parseFromString(html, "text/html");
                    const newContent =
                        doc.getElementById("detail-pane").innerHTML;
                    if (detailPane) detailPane.innerHTML = newContent;
                })
                .catch((error) =>
                    console.error("Error loading details:", error),
                );
        });
    });

    // ブラウザの「戻る」操作時にページをリロードして整合性を保つ
    window.addEventListener("popstate", () => {
        window.location.reload();
    });

    /**
     * フィルタを適用してリストの表示/非表示を切り替える
     */
    function applyFilter(status) {
        // サイドバーのアクティブ状態切り替え
        filterLinks.forEach((link) => {
            if (link.dataset.filterType === status) {
                link.classList.add("active");
            } else {
                link.classList.remove("active");
            }
        });

        // リストアイテムの表示切り替え
        let visibleCount = 0;
        surveyItems.forEach((item) => {
            const itemStatus = item.dataset.surveyStatus;
            if (status === "ALL" || itemStatus === status) {
                item.style.display = ""; // 表示
                visibleCount++;
            } else {
                item.style.display = "none"; // 非表示
            }

            // 詳細リンクのURLパラメータを更新(リロード後もフィルタ状態を維持するため)
            const url = new URL(item.href);
            if (status === "ALL") {
                url.searchParams.delete("status");
            } else {
                url.searchParams.set("status", status);
            }
            item.href = url.toString();
        });

        // 該当なしメッセージの表示制御
        if (noDataMessage) {
            noDataMessage.style.display =
                visibleCount === 0 && surveyItems.length > 0 ? "block" : "none";
        }
    }

    /**
     * URLのクエリパラメータを更新する(リロードなし)
     */
    function updateUrl(status) {
        const newUrl = new URL(window.location);
        if (status === "ALL") {
            newUrl.searchParams.delete("status");
        } else {
            newUrl.searchParams.set("status", status);
        }
        window.history.pushState({}, "", newUrl);
    }
});

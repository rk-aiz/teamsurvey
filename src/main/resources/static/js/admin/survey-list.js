/**
 * アンケート一覧画面用スクリプト
 * クライアントサイドでのフィルタリング機能を提供
 */
document.addEventListener('DOMContentLoaded', () => {
    const filterLinks = document.querySelectorAll('[data-filter-type]');
    const surveyItems = document.querySelectorAll('.survey-item');
    const noDataMessage = document.getElementById('no-surveys-message');

    // URLパラメータから初期ステータスを取得
    const urlParams = new URLSearchParams(window.location.search);
    const initialStatus = urlParams.get('status') || 'ALL';

    // 初期表示
    applyFilter(initialStatus);

    // フィルタボタンのクリックイベント
    filterLinks.forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            const status = link.dataset.filterType;
            
            applyFilter(status);
            updateUrl(status);
        });
    });

    /**
     * フィルタを適用してリストの表示/非表示を切り替える
     */
    function applyFilter(status) {
        // サイドバーのアクティブ状態切り替え
        filterLinks.forEach(link => {
            if (link.dataset.filterType === status) {
                link.classList.add('active');
            } else {
                link.classList.remove('active');
            }
        });

        // リストアイテムの表示切り替え
        let visibleCount = 0;
        surveyItems.forEach(item => {
            const itemStatus = item.dataset.surveyStatus;
            if (status === 'ALL' || itemStatus === status) {
                item.style.display = ''; // 表示
                visibleCount++;
            } else {
                item.style.display = 'none'; // 非表示
            }
            
            // 詳細リンクのURLパラメータを更新（リロード後もフィルタ状態を維持するため）
            const url = new URL(item.href);
            if (status === 'ALL') {
                url.searchParams.delete('status');
            } else {
                url.searchParams.set('status', status);
            }
            item.href = url.toString();
        });

        // 該当なしメッセージの表示制御
        if (noDataMessage) {
            noDataMessage.style.display = (visibleCount === 0 && surveyItems.length > 0) ? 'block' : 'none';
        }
    }

    /**
     * URLのクエリパラメータを更新する（リロードなし）
     */
    function updateUrl(status) {
        const newUrl = new URL(window.location);
        if (status === 'ALL') {
            newUrl.searchParams.delete('status');
        } else {
            newUrl.searchParams.set('status', status);
        }
        window.history.pushState({}, '', newUrl);
    }
});
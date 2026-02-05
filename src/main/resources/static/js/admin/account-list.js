/**
 * アカウント一覧画面用スクリプト
 */
document.addEventListener('DOMContentLoaded', () => {
    const mainElement = document.querySelector('main[data-show-modal]');
    const accountModalEl = document.getElementById('accountModal');
    
    if (mainElement && accountModalEl) {
        const showModal = mainElement.dataset.showModal === 'true';
        
        if (showModal) {
            const myModal = new bootstrap.Modal(accountModalEl);
            myModal.show();

            // モーダルが閉じられた時に一覧に戻る（URLパラメータクリア）
            accountModalEl.addEventListener('hidden.bs.modal', function () {
                // 現在のパス（クエリパラメータなし）を取得して遷移
                const path = window.location.pathname;
                window.location.href = path;
            });
        }
    }
});
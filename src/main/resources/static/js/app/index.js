/**
 * Posts Management Application
 * Modern JavaScript implementation using ES6+ features
 */
class PostsApp {
    constructor() {
        this.currentPage = 0;
        this.pageSize = 10;
        this.searchKeyword = '';
        this.currentPostId = null;
        this.init();
    }

    init() {
        // Event Listeners for Posts
        const btnSave = document.getElementById('btn-save');
        const btnUpdate = document.getElementById('btn-update');
        const btnDelete = document.getElementById('btn-delete');
        const btnSearch = document.getElementById('btn-search');
        const btnSearchClear = document.getElementById('btn-search-clear');
        const btnMyPosts = document.getElementById('btn-my-posts');
        const btnPopular = document.getElementById('btn-popular');
        const searchKeyword = document.getElementById('searchKeyword');

        if (btnSave) {
            btnSave.addEventListener('click', () => this.save());
        }

        if (btnUpdate) {
            btnUpdate.addEventListener('click', () => this.update());
        }

        if (btnDelete) {
            btnDelete.addEventListener('click', () => this.delete());
        }

        if (btnSearch) {
            btnSearch.addEventListener('click', () => this.search());
        }

        if (btnSearchClear) {
            btnSearchClear.addEventListener('click', () => this.clearSearch());
        }

        if (btnMyPosts) {
            btnMyPosts.addEventListener('click', () => this.loadMyPosts());
        }

        if (btnPopular) {
            btnPopular.addEventListener('click', () => this.loadPopularPosts());
        }

        if (searchKeyword) {
            searchKeyword.addEventListener('keypress', (e) => {
                if (e.key === 'Enter') {
                    this.search();
                }
            });
        }

        // Event Listeners for Comments
        const btnCommentSave = document.getElementById('btn-comment-save');
        if (btnCommentSave) {
            const postId = document.getElementById('id')?.value;
            if (postId) {
                this.currentPostId = postId;
                btnCommentSave.addEventListener('click', () => this.saveComment());
                this.loadComments();
            }
        }
    }

    async save() {
        const data = {
            title: document.getElementById('title').value,
            content: document.getElementById('content').value
        };

        try {
            const response = await fetch('/api/v1/posts', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                },
                body: JSON.stringify(data)
            });

            if (!response.ok) {
                const error = await response.json();
                throw new Error(JSON.stringify(error));
            }

            alert('글이 등록되었습니다.');
            window.location.href = '/';
        } catch (error) {
            console.error('Error saving post:', error);
            alert('글 등록 중 오류가 발생했습니다.\n' + error.message);
        }
    }

    async update() {
        const id = document.getElementById('id').value;
        const data = {
            title: document.getElementById('title').value,
            content: document.getElementById('content').value
        };

        try {
            const response = await fetch(`/api/v1/posts/${id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                },
                body: JSON.stringify(data)
            });

            if (!response.ok) {
                const error = await response.json();
                throw new Error(JSON.stringify(error));
            }

            alert('글이 수정되었습니다.');
            window.location.href = '/';
        } catch (error) {
            console.error('Error updating post:', error);
            alert('글 수정 중 오류가 발생했습니다.\n' + error.message);
        }
    }

    async delete() {
        const id = document.getElementById('id').value;

        if (!confirm('정말 삭제하시겠습니까?')) {
            return;
        }

        try {
            const response = await fetch(`/api/v1/posts/${id}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                }
            });

            if (!response.ok) {
                const error = await response.json();
                throw new Error(JSON.stringify(error));
            }

            alert('글이 삭제되었습니다.');
            window.location.href = '/';
        } catch (error) {
            console.error('Error deleting post:', error);
            alert('글 삭제 중 오류가 발생했습니다.\n' + error.message);
        }
    }

    async search() {
        const keyword = document.getElementById('searchKeyword').value;
        if (!keyword || keyword.trim() === '') {
            alert('검색어를 입력하세요.');
            return;
        }
        this.searchKeyword = keyword;
        this.currentPage = 0;
        await this.loadSearchResults();
    }

    async clearSearch() {
        this.searchKeyword = '';
        document.getElementById('searchKeyword').value = '';
        window.location.href = '/';
    }

    async loadSearchResults() {
        try {
            const response = await fetch(
                `/api/v1/posts/search?keyword=${encodeURIComponent(this.searchKeyword)}&page=${this.currentPage}&size=${this.pageSize}`
            );

            if (!response.ok) {
                throw new Error('검색 실패');
            }

            const data = await response.json();
            this.renderPostsList(data);
        } catch (error) {
            console.error('Error searching posts:', error);
            alert('검색 중 오류가 발생했습니다.');
        }
    }

    async loadMyPosts() {
        try {
            const response = await fetch('/api/v1/posts/my');

            if (!response.ok) {
                throw new Error('내 글 조회 실패');
            }

            const posts = await response.json();
            this.renderSimplePostsList(posts, '내 글 목록');
        } catch (error) {
            console.error('Error loading my posts:', error);
            alert('내 글을 불러오는 중 오류가 발생했습니다.');
        }
    }

    async loadPopularPosts() {
        try {
            const response = await fetch('/api/v1/posts/popular?limit=10');

            if (!response.ok) {
                throw new Error('인기글 조회 실패');
            }

            const posts = await response.json();
            this.renderSimplePostsList(posts, '인기글 TOP 10');
        } catch (error) {
            console.error('Error loading popular posts:', error);
            alert('인기글을 불러오는 중 오류가 발생했습니다.');
        }
    }

    renderPostsList(data) {
        const tbody = document.getElementById('tbody');
        tbody.innerHTML = '';

        if (data.posts.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5" class="text-center">검색 결과가 없습니다.</td></tr>';
            return;
        }

        data.posts.forEach(post => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${post.id}</td>
                <td><a href="/posts/view/${post.id}">${this.escapeHtml(post.title)}</a></td>
                <td>${this.escapeHtml(post.author)}</td>
                <td>${post.viewCount}</td>
                <td>${this.formatDate(post.modifiedDate)}</td>
            `;
            tbody.appendChild(row);
        });

        this.renderPagination(data);
    }

    renderSimplePostsList(posts, title) {
        const tbody = document.getElementById('tbody');
        tbody.innerHTML = '';

        if (posts.length === 0) {
            tbody.innerHTML = `<tr><td colspan="5" class="text-center">${title}이(가) 없습니다.</td></tr>`;
            return;
        }

        posts.forEach(post => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${post.id}</td>
                <td><a href="/posts/view/${post.id}">${this.escapeHtml(post.title)}</a></td>
                <td>${this.escapeHtml(post.author)}</td>
                <td>${post.viewCount}</td>
                <td>${this.formatDate(post.modifiedDate)}</td>
            `;
            tbody.appendChild(row);
        });

        document.getElementById('pagination-nav').style.display = 'none';
    }

    renderPagination(data) {
        const pagination = document.getElementById('pagination');
        const paginationNav = document.getElementById('pagination-nav');

        if (!pagination || !paginationNav) return;

        pagination.innerHTML = '';

        if (data.totalPages <= 1) {
            paginationNav.style.display = 'none';
            return;
        }

        paginationNav.style.display = 'block';

        // Previous button
        const prevLi = document.createElement('li');
        prevLi.className = `page-item ${!data.hasPrevious ? 'disabled' : ''}`;
        prevLi.innerHTML = `<a class="page-link" href="#">이전</a>`;
        if (data.hasPrevious) {
            prevLi.addEventListener('click', (e) => {
                e.preventDefault();
                this.currentPage--;
                this.loadSearchResults();
            });
        }
        pagination.appendChild(prevLi);

        // Page numbers
        const startPage = Math.max(0, data.currentPage - 2);
        const endPage = Math.min(data.totalPages - 1, data.currentPage + 2);

        for (let i = startPage; i <= endPage; i++) {
            const li = document.createElement('li');
            li.className = `page-item ${i === data.currentPage ? 'active' : ''}`;
            li.innerHTML = `<a class="page-link" href="#">${i + 1}</a>`;
            li.addEventListener('click', (e) => {
                e.preventDefault();
                this.currentPage = i;
                this.loadSearchResults();
            });
            pagination.appendChild(li);
        }

        // Next button
        const nextLi = document.createElement('li');
        nextLi.className = `page-item ${!data.hasNext ? 'disabled' : ''}`;
        nextLi.innerHTML = `<a class="page-link" href="#">다음</a>`;
        if (data.hasNext) {
            nextLi.addEventListener('click', (e) => {
                e.preventDefault();
                this.currentPage++;
                this.loadSearchResults();
            });
        }
        pagination.appendChild(nextLi);
    }

    // Comment methods
    async loadComments() {
        if (!this.currentPostId) return;

        try {
            const response = await fetch(`/api/v1/posts/${this.currentPostId}/comments`);

            if (!response.ok) {
                throw new Error('댓글 조회 실패');
            }

            const comments = await response.json();
            this.renderComments(comments);

            // Update comment count
            const countResponse = await fetch(`/api/v1/posts/${this.currentPostId}/comments/count`);
            const count = await countResponse.json();
            document.getElementById('comment-count').textContent = count;
        } catch (error) {
            console.error('Error loading comments:', error);
        }
    }

    renderComments(comments) {
        const commentsList = document.getElementById('comments-list');
        if (!commentsList) return;

        commentsList.innerHTML = '';

        if (comments.length === 0) {
            commentsList.innerHTML = '<p class="text-muted">첫 댓글을 작성해보세요!</p>';
            return;
        }

        comments.forEach(comment => {
            const commentDiv = document.createElement('div');
            commentDiv.className = 'card mb-2';
            commentDiv.innerHTML = `
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-start">
                        <div>
                            <strong>${this.escapeHtml(comment.author)}</strong>
                            <small class="text-muted ms-2">${this.formatDate(comment.createdDate)}</small>
                        </div>
                        <div class="comment-actions" data-comment-id="${comment.id}" data-author-email="${comment.authorEmail}">
                            <button class="btn btn-sm btn-outline-secondary btn-comment-edit">수정</button>
                            <button class="btn btn-sm btn-outline-danger btn-comment-delete">삭제</button>
                        </div>
                    </div>
                    <p class="mt-2 mb-0 comment-content">${this.escapeHtml(comment.content)}</p>
                    <textarea class="form-control mt-2 comment-edit-area" style="display:none;">${this.escapeHtml(comment.content)}</textarea>
                    <div class="mt-2 comment-edit-buttons" style="display:none;">
                        <button class="btn btn-sm btn-primary btn-comment-save-edit">저장</button>
                        <button class="btn btn-sm btn-secondary btn-comment-cancel-edit">취소</button>
                    </div>
                </div>
            `;
            commentsList.appendChild(commentDiv);
        });

        this.attachCommentEventListeners();
    }

    attachCommentEventListeners() {
        document.querySelectorAll('.btn-comment-edit').forEach(btn => {
            btn.addEventListener('click', (e) => this.editComment(e.target));
        });

        document.querySelectorAll('.btn-comment-delete').forEach(btn => {
            btn.addEventListener('click', (e) => this.deleteComment(e.target));
        });

        document.querySelectorAll('.btn-comment-save-edit').forEach(btn => {
            btn.addEventListener('click', (e) => this.saveCommentEdit(e.target));
        });

        document.querySelectorAll('.btn-comment-cancel-edit').forEach(btn => {
            btn.addEventListener('click', (e) => this.cancelCommentEdit(e.target));
        });
    }

    async saveComment() {
        const content = document.getElementById('comment-content').value;

        if (!content || content.trim() === '') {
            alert('댓글 내용을 입력하세요.');
            return;
        }

        try {
            const response = await fetch(`/api/v1/posts/${this.currentPostId}/comments`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                },
                body: JSON.stringify({ content })
            });

            if (!response.ok) {
                const error = await response.json();
                throw new Error(error.message || '댓글 등록 실패');
            }

            document.getElementById('comment-content').value = '';
            await this.loadComments();
        } catch (error) {
            console.error('Error saving comment:', error);
            alert('댓글 등록 중 오류가 발생했습니다.\n' + error.message);
        }
    }

    editComment(button) {
        const card = button.closest('.card-body');
        card.querySelector('.comment-content').style.display = 'none';
        card.querySelector('.comment-edit-area').style.display = 'block';
        card.querySelector('.comment-edit-buttons').style.display = 'block';
        card.querySelector('.comment-actions').style.display = 'none';
    }

    cancelCommentEdit(button) {
        const card = button.closest('.card-body');
        card.querySelector('.comment-content').style.display = 'block';
        card.querySelector('.comment-edit-area').style.display = 'none';
        card.querySelector('.comment-edit-buttons').style.display = 'none';
        card.querySelector('.comment-actions').style.display = 'block';
    }

    async saveCommentEdit(button) {
        const card = button.closest('.card-body');
        const commentId = card.querySelector('.comment-actions').dataset.commentId;
        const content = card.querySelector('.comment-edit-area').value;

        if (!content || content.trim() === '') {
            alert('댓글 내용을 입력하세요.');
            return;
        }

        try {
            const response = await fetch(`/api/v1/posts/${this.currentPostId}/comments/${commentId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                },
                body: JSON.stringify({ content })
            });

            if (!response.ok) {
                const error = await response.json();
                throw new Error(error.message || '댓글 수정 실패');
            }

            await this.loadComments();
        } catch (error) {
            console.error('Error updating comment:', error);
            alert('댓글 수정 중 오류가 발생했습니다.\n' + error.message);
        }
    }

    async deleteComment(button) {
        if (!confirm('댓글을 삭제하시겠습니까?')) {
            return;
        }

        const commentId = button.closest('.comment-actions').dataset.commentId;

        try {
            const response = await fetch(`/api/v1/posts/${this.currentPostId}/comments/${commentId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                }
            });

            if (!response.ok) {
                const error = await response.json();
                throw new Error(error.message || '댓글 삭제 실패');
            }

            await this.loadComments();
        } catch (error) {
            console.error('Error deleting comment:', error);
            alert('댓글 삭제 중 오류가 발생했습니다.\n' + error.message);
        }
    }

    // Utility methods
    escapeHtml(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }

    formatDate(dateString) {
        const date = new Date(dateString);
        return date.toLocaleString('ko-KR', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit'
        });
    }
}

// DOM이 로드되면 앱 초기화
document.addEventListener('DOMContentLoaded', () => {
    new PostsApp();
});

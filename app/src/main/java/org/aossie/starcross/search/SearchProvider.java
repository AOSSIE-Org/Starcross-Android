package org.aossie.starcross.search;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Set;

public class SearchProvider extends ContentProvider {
    public static class SearchTerm {
        String origin;
        String query;

        public SearchTerm(String query, String origin) {
            this.query = query;
            this.origin = origin;
        }
    }

    public static String AUTHORITY = "org.aossie.starcross";
    private static final int SEARCH_SUGGEST = 0;
    private static final UriMatcher uriMatcher = buildUriMatcher();

    private static final String[] COLUMNS = {"_id",
            SearchManager.SUGGEST_COLUMN_QUERY,
            SearchManager.SUGGEST_COLUMN_TEXT_1,
            SearchManager.SUGGEST_COLUMN_TEXT_2};

    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGEST);
        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH_SUGGEST);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        if (!TextUtils.isEmpty(selection)) {
            throw new IllegalArgumentException("selection not allowed for " + uri);
        }
        if (selectionArgs != null && selectionArgs.length != 0) {
            throw new IllegalArgumentException("selectionArgs not allowed for " + uri);
        }
        if (!TextUtils.isEmpty(sortOrder)) {
            throw new IllegalArgumentException("sortOrder not allowed for " + uri);
        }
        if (uriMatcher.match(uri) == SEARCH_SUGGEST) {
            String query = null;
            if (uri.getPathSegments().size() > 1) {
                query = uri.getLastPathSegment();
            }
            return getSuggestions(query);
        }
        throw new IllegalArgumentException("Unknown URL " + uri);
    }

    private Cursor getSuggestions(String query) {
        MatrixCursor cursor = new MatrixCursor(COLUMNS);
        if (query == null) {
            return cursor;
        }
        Set<SearchTerm> results = null; // TODO makhanov
        if (results != null) {
            Log.d("SearchProvider", "Got results n=" + results.size());
            for (SearchTerm result : results) {
                cursor.addRow(columnValuesOfSuggestion(result));
            }
        }
        return cursor;
    }

    private static int s = 0;

    private Object[] columnValuesOfSuggestion(SearchTerm suggestion) {
        return new String[]{Integer.toString(s++),
                suggestion.query,
                suggestion.query,
                suggestion.origin,
        };
    }

    @Override
    public String getType(@NonNull Uri uri) {
        if (uriMatcher.match(uri) == SEARCH_SUGGEST) {
            return SearchManager.SUGGEST_MIME_TYPE;
        }
        throw new IllegalArgumentException("Unknown URL " + uri);
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }
}

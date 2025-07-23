package com.example.path_finder;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.Arrays;

public class MapRun extends AppCompatActivity {
    EditText src;
    EditText des;
    Button btn;
    MapView map;
    String[] locations= {"Baguiati", "Kestopur", "DumDumpark", "Bangur"};
    GeoPoint[] points= {
            new GeoPoint(22.6152, 88.4314),
            new GeoPoint(22.6050, 88.4290),
            new GeoPoint(22.6100, 88.4080),
            new GeoPoint(22.5965, 88.4038),
    };
    int[][]graph={
            {0, 2, 3, 0},
            {2, 0, 0, 4},
            {3, 0, 0, 1},
            {0, 4, 1, 0}
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        src = findViewById(R.id.source);
        des = findViewById(R.id.destination);
        btn = findViewById(R.id.button);
        map = findViewById(R.id.map);
        map.setMultiTouchControls(true);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String start = src.getText().toString().trim();
                String end = des.getText().toString().trim();
                int srcIndex = getIndex(start);
                int desIndex = getIndex(end);
                if (srcIndex == -1 || desIndex == -1) {
                    Toast.makeText(MapRun.this, "Locaction is not found",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                map.getOverlays().clear();
                showMarkers();
                int[] parent= Primsalgo(graph);
                for (int i = 1; i < parent.length; i++) {
                    if (parent[i] != -1) {
                        drawLineBetween(points[i], points[parent[i]]);
                    }
                }
            }
        });
    }
    private void showMarkers() {
        map.getOverlays().clear();
        for (int i = 0; i < points.length; i++) {
            Marker marker = new Marker(map);
            marker.setPosition(points[i]);
            marker.setTitle(locations[i]);
            map.getOverlays().add(marker);
        }
        map.getController().setZoom(14.5);
        map.getController().setCenter(points[0]);
    }

    private int getIndex(String name) {
        for (int i = 0; i < locations.length; i++) {
            if (locations[i].equalsIgnoreCase(name)) return i;
        }
        return -1;
    }
    private void drawLineBetween(GeoPoint p1, GeoPoint p2) {
        Polyline line = new Polyline();
        line.setColor(Color.BLUE);
        line.setWidth(5f);
        line.setPoints(Arrays.asList(p1, p2));
        map.getOverlays().add(line);
        map.invalidate();
    }
    private int[] Primsalgo(int[][] graph) {
        int v = graph.length;
        int[] key = new int[v];
        int[] parent = new int[v];
        boolean[] mst = new boolean[v];
        Arrays.fill(key, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);
        key[0] = 0;
        for (int c = 0; c < v - 1; c++) {
            int u = minkey(key, mst);
            mst[u] = true;
            for (int x = 0; x < v; x++) {
                if (graph[u][x] != 0 && !mst[x] && graph[u][x] < key[x]) {
                    parent[x] = u;
                    key[x] = graph[u][x];

                }
            }
        }
        return parent;
    }
    private int minkey(int[] key, boolean[]mst) {
        int min = Integer.MAX_VALUE, minIndex = -1;
        for (int v = 0; v < key.length; v++) {
            if (!mst[v] && key[v] < min) {
                min = key[v];
                minIndex = v;
            }
        }
        return minIndex;
    }

}

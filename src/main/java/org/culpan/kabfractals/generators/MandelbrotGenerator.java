package org.culpan.kabfractals.generators;

import javafx.concurrent.Task;

public class MandelbrotGenerator extends Task<int[][]> {
    protected  int width;

    protected int height;

    protected int iterations;

    protected FractalSpace fractalSpace;

    public MandelbrotGenerator(FractalSpace fractalSpace, int iterations) {
        this.fractalSpace = fractalSpace;
        this.width = fractalSpace.getWidth();
        this.height = fractalSpace.getHeight();
        this.iterations = iterations;
    }

    @Override
    protected int[][] call() throws Exception {
        int[][] result = new int[height][width];

        double Re_factor = (fractalSpace.getRealMax() - fractalSpace.getRealMin()) / (width - 1);
        double Im_factor = (fractalSpace.getImMax() - fractalSpace.getImMin()) / (height - 1);

        for (int y = 0; y < height; ++y)
        {
            double c_im = fractalSpace.getImMax() - y * Im_factor;
            for (int x = 0; x < width; ++x)
            {
                double c_re = fractalSpace.getRealMin() + x * Re_factor;

                double Z_re = c_re, Z_im = c_im;
                boolean isInside = true;

                int n;
                for (n = 0; n < iterations; ++n)
                {
                    double Z_re2 = Z_re * Z_re, Z_im2 = Z_im * Z_im;
                    if (Z_re2 + Z_im2 > 4)
                    {
                        isInside = false;
                        break;
                    }
                    Z_im = 2 * Z_re * Z_im + c_im;
                    Z_re = Z_re2 - Z_im2 + c_re;
                }
                if (isInside)
                {
                    result[y][x] = 0;
                }
                else
                {
                    result[y][x] = n;
                }
            }
        }

        return result;
    }

}

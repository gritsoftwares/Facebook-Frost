public static void fadeIn(Context c, View v, int offset, int duration) {
        v.setVisibility(View.VISIBLE);
        v.startAnimation(fadeInAnimation(c, offset, duration));
        }

private static Animation fadeInAnimation(Context c, int offset, int duration) {
        Animation fadeInAnimation = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);
        fadeInAnimation.setStartOffset(offset);
        fadeInAnimation.setDuration(duration);
        return fadeInAnimation;
        }

public static void fadeOut(Context c, View v, int offset, int duration) {
        v.setVisibility(View.GONE);
        v.startAnimation(fadeOutAnimation(c, offset, duration));
        }

private static Animation fadeOutAnimation(Context c, int offset, int duration) {
        Animation fadeOutAnimation = AnimationUtils.loadAnimation(c, android.R.anim.fade_out);
        fadeOutAnimation.setStartOffset(offset);
        fadeOutAnimation.setDuration(duration);
        return fadeOutAnimation;
        }

public static void circleReveal(View view, int t) {
        int x = view.getWidth() / 2;
        int y = view.getHeight() / 2;
        Log.e("radius", x + " " + y);
        circleReveal(view, x, y, t);
        }

public static void circleReveal(View view, int x, int y, int t) {
// previously invisible view
final View myView = view;
        int finalRadius = Math.max(view.getWidth(), view.getHeight());
        Log.e("final radius", finalRadius + " ");

        // create the animator for this view (the start radius is zero)
        Animator anim =
        ViewAnimationUtils.createCircularReveal(myView, x, y, 0, finalRadius).setDuration(t);

        // make the view visible and start the animation
        myView.setVisibility(View.VISIBLE);
        anim.start();
        }

public static void exitReveal(View view, int t) {
        int x = view.getWidth() / 2;
        int y = view.getHeight() / 2;
        exitReveal(view, x, y, t);
        }

public static void exitReveal(View view, int x, int y, int t) {
// previously visible view
final View myView = view;

        // get the initial radius for the clipping circle
        int initialRadius = Math.max(myView.getWidth(), myView.getHeight());

        // create the animation (the final radius is zero)
        Animator anim =
        ViewAnimationUtils.createCircularReveal(myView, x, y, initialRadius, 0).setDuration(t);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
@Override
public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        myView.setVisibility(View.GONE);
        }
        });

        // start the animation
        anim.start();
        }